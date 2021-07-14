package com.general;

import com.general.bean.HelloWorld;
import com.general.extension.pack.PackTypeExt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class NbheraDemoApplicationTests {
    @Resource
    HelloWorld helloWorld;
    @Resource
    PackTypeExt packTypeExt;

    @Test
    void contextLoads() {
        System.out.println(helloWorld.getHelloWorld());
        String packType1 = packTypeExt.reduce(1).getPackType();
        String packType2 = packTypeExt.reduce(2).getPackType();
        System.out.println(packType1);
        System.out.println(packType2);
    }

}
