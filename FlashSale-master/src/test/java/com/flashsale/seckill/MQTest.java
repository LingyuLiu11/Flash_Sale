package com.flashsale.seckill;

import com.flashsale.seckill.mq.RocketMQService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class  MQTest {
    @Resource
    private RocketMQService rocketMQService;

    @Test
    public void sendMQTest() throws Exception {
        rocketMQService.sendMessage("test-flashsale", "Hello World!" + new Date().toString());
    }
}
