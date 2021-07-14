package com.general;

import com.general.bean.HelloWorld;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class NbheraDemoApplicationTests {
    @Resource
    HelloWorld helloWorld;

    @Test
    void contextLoads() {
        System.out.println(helloWorld.getHelloWorld());
    }

}
