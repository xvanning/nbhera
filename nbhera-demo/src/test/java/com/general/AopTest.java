package com.general;

import com.general.aop.AopDemo;
import com.general.aop.ClientTestReq;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author xvanning
 * date: 2021/8/25 22:38
 * desc:
 */
@SpringBootTest
public class AopTest {
    @Resource
    AopDemo aopDemo;

    @Test
    public void testNbClientLog() {
        ClientTestReq req = new ClientTestReq();
        req.setId(1);
        req.setName("测试");
        System.out.println(aopDemo.getNbclientTest(req));
    }

    @Test
    public void testNbTest() {
        System.out.println(aopDemo.getNbTest(10, "测试"));
    }

}
