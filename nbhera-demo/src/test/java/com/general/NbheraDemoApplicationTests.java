package com.general;

import com.general.extension.biz.vertical.VerticalBizDemo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class NbheraDemoApplicationTests {
    @Resource
    VerticalBizDemo verticalBizDemo;

    @Test
    void getPackTypeByStrategy() {
        String packType1 = verticalBizDemo.getPackTypeByStrategy(1);
        System.out.println(packType1);
        System.out.println("================ 策略模式例子1，随意任何KEY ===============");
        String packType2 = verticalBizDemo.getPackTypeByStrategy(2);
        System.out.println(packType2);
    }

    @Test
    void getPackTypeByStrategyWithHorizontalExt() {
        String packType1 = verticalBizDemo.getPackTypeByStrategyWithHorizontalExt(1);
        System.out.println(packType1);
        System.out.println("================ 策略模式例子2，直接使用已有的水平模板KEY ===============");
        String packType2 = verticalBizDemo.getPackTypeByStrategyWithHorizontalExt(2);
        System.out.println(packType2);
    }

    @Test
    void getCurrentBiz() {
        String currentBiz1 = verticalBizDemo.getCurrentBiz(1);
        System.out.println(currentBiz1);
        System.out.println("================ 垂直业务例子1，只有独立的垂直模板 ===============");
        String currentBiz2 = verticalBizDemo.getCurrentBiz(2);
        System.out.println(currentBiz2);
    }

    @Test
    void getTransportTypeWithoutStrategy() {
        String schoolCourier = verticalBizDemo.getTransportTypeWithoutStrategy(1, 1);
        System.out.println(schoolCourier);
        String schoolCar = verticalBizDemo.getTransportTypeWithoutStrategy(1, 2);
        System.out.println(schoolCar);
        System.out.println("================ 垂直业务例子2，只有独立的水平模板，需要垂直模板进行订阅，无关垂直业务 ===============");
        String communityCourier = verticalBizDemo.getTransportTypeWithoutStrategy(2, 1);
        System.out.println(communityCourier);
        String communityCar = verticalBizDemo.getTransportTypeWithoutStrategy(2, 2);
        System.out.println(communityCar);
    }

}
