package com.general;

import com.general.extension.biz.vertical.VerticalBizDemo;
import com.general.extension.strategy.PackTypeExt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class NbheraDemoApplicationTests {
    @Resource
    PackTypeExt packTypeExt;
    @Resource
    VerticalBizDemo verticalBizDemo;

    @Test
    void testStrategy() {
        String packType1 = packTypeExt.reduce(1).getPackType();
        System.out.println(packType1);
        System.out.println("=========== 我是分割线 ==========");
        String packType2 = packTypeExt.reduce(2).getPackType();
        System.out.println(packType2);
    }

    @Test
    void testVerticalBiz() {
        String currentBiz1 = verticalBizDemo.getCurrentBiz(1);
        System.out.println(currentBiz1);
        System.out.println("=========== 我是分割线 ==========");
        String currentBiz2 = verticalBizDemo.getCurrentBiz(2);
        System.out.println(currentBiz2);
    }

}
