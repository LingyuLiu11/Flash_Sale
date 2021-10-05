package com.flashsale.seckill.services;

import com.alibaba.fastjson.JSON;
import com.flashsale.seckill.db.dao.OrderDao;
import com.flashsale.seckill.db.dao.SeckillActivityDao;
import com.flashsale.seckill.db.dao.SeckillCommodityDao;
import com.flashsale.seckill.db.po.Order;
import com.flashsale.seckill.db.po.SeckillActivity;
import com.flashsale.seckill.db.po.SeckillCommodity;
import com.flashsale.seckill.mq.RocketMQService;
import com.flashsale.seckill.util.RedisService;
import com.flashsale.seckill.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class SeckillActivityService {

    @Resource
    private OrderDao orderDao;


    @Resource
    private RedisService redisService;

    @Resource
    private SeckillCommodityDao seckillCommodityDao;


    @Resource
    private SeckillActivityDao seckillActivityDao;

    @Resource
    private RocketMQService rocketMQService;
    /*
     * datacenterId; 数据中心
     * machineId; 机器标识
     * 在分布式环境中可以从机器配置上读取
     * 单机开发环境中先写死
     */
    private SnowFlake snowFlake = new SnowFlake(1, 1);

/*
 * 创建订单
 *
 * @param seckillActivityId
 * * @param userId
 * * @return
 * * @throws Exception
  */


    public Order createOrder(long seckillActivityId, long userId) throws Exception {
 /*
         * * 1.创建订单
         * */
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        Order order = new Order();
        //采用雪花算法生成订单ID
        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setSeckillActivityId(seckillActivity.getId());
        order.setUserId(userId);
        order.setOrderAmount(seckillActivity.getSeckillPrice().longValue());
 /*
         * *2.发送创建订单消息
         * */
        rocketMQService.sendMessage("seckill_order", JSON.toJSONString(order));

        //3.发送订单付款状态校验消息
        rocketMQService.sendDelayMessage("pay_check", JSON.toJSONString(order),5);
        return order;


 }


    public boolean seckillStockValidator(long activityId) {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }



    /**
     * 订单支付完成处理
     *
     * @param orderNo
     */
    public void payOrderProcess(String orderNo) throws Exception {
        log.info("完成支付订单 订单号：" + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        /*
         * 1.判断订单是否存在
         * 2.判断订单状态是否为未支付状态
         */
        if (order == null) {
            log.error("订单号对应订单不存在：" + orderNo);
            return;
        } else if(order.getOrderStatus() != 1 ) {
            log.error("订单状态无效：" + orderNo);
            return;
        }
        /*
         * 2.订单支付完成
         */
        order.setPayTime(new Date());
//订单状态 0:没有可用库存，无效订单 1:已创建等待付款 ,2:支付完成
        order.setOrderStatus(2);
        orderDao.updateOrder(order);
        /*
         *3.发送订单付款成功消息
         */
        rocketMQService.sendMessage("pay_done", JSON.toJSONString(order));
    }

    /**
     * 将秒杀详情相关信息倒入redis
     * @param seckillActivityId
     */
    public void pushSeckillInfoToRedis(long seckillActivityId) {
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        redisService.setValue("seckillActivity:" + seckillActivityId, JSON.toJSONString(seckillActivity));
        //SeckillCommodityDao seckillCommodityDao;
        SeckillCommodity seckillCommodity = seckillCommodityDao.querySeckillCommodityById(seckillActivity.getCommodityId());
        redisService.setValue("seckillCommodity:" + seckillActivity.getCommodityId(), JSON.toJSONString(seckillCommodity));
    }
}

