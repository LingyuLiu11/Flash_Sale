package com.flashsale.seckill.db.dao;

import com.flashsale.seckill.db.po.Order;

public interface OrderDao {

    public void insertOrder(Order order);

    public Order queryOrder(String orderNo);

    public void updateOrder(Order order);
}
