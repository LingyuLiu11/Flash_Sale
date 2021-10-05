package com.flashsale.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//@SpringBootApplication
@SpringBootApplication
@MapperScan("com.flashsale.seckill.db.mappers")
@ComponentScan(basePackages = {"com.flashsale"})


public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
}
