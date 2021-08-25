package com.general.aop;

import com.general.integration.annotation.NbClientLog;
import com.general.test.annotation.NbTest;
import org.springframework.stereotype.Component;

/**
 * @author xvanning
 * date: 2021/8/25 22:40
 * desc: aop demo
 */
@Component
public class AopDemo {

    /**
     * 测试NbClientLog
     *
     * @param clientTestReq clientTestReq
     * @return ClientTestResp
     */
    @NbClientLog
    public ClientTestResp getNbclientTest(ClientTestReq clientTestReq) {
        return ClientTestResp.builder().result("result").sequence("1111").build();
    }

    /**
     * 测试 NbTest
     *
     * @param age  age
     * @param name name
     * @return ClientTestResp
     */
    @NbTest(expression = "age==10")
    public ClientTestResp getNbTest(Integer age, String name) {
        return ClientTestResp.builder().result("result").sequence("1111").build();
    }
}
