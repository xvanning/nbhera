package com.general;

import com.alibaba.fastjson.JSON;
import com.general.enums.BusinessTypeEnums;
import com.general.enums.PackTypeEnums;
import com.general.enums.PayTypeEnums;
import com.general.enums.TransTypeEnums;
import com.general.extension.Reducers;
import com.general.extension.biz.vertical.VerticalBizDemo;
import com.general.extension.scenario.checkrules.rules.CheckProcessExt;
import com.general.extension.template.ScenarioConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class NbheraDemoApplicationTests {
    @Resource
    VerticalBizDemo verticalBizDemo;

    @Test
    void getPackTypeByStrategy() {
        String smallPack = verticalBizDemo.getPackTypeByStrategy(PackTypeEnums.SMALL.getCode());
        System.out.println(smallPack);
        System.out.println("================ 策略模式例子1，随意任何KEY ===============");
        String bigPack = verticalBizDemo.getPackTypeByStrategy(PackTypeEnums.BIG.getCode());
        System.out.println(bigPack);
    }

    @Test
    void getPackTypeByStrategyWithHorizontalExt() {
        String courierPack = verticalBizDemo.getPackTypeByStrategyWithHorizontalExt(TransTypeEnums.COURIER.getCode());
        System.out.println(courierPack);
        System.out.println("================ 策略模式例子2，直接使用已有的水平模板KEY ===============");
        String carPack = verticalBizDemo.getPackTypeByStrategyWithHorizontalExt(TransTypeEnums.CAR.getCode());
        System.out.println(carPack);
    }

    @Test
    void getCurrentBiz() {
        String schoolBiz = verticalBizDemo.getCurrentBiz(BusinessTypeEnums.SCHOOL.getCode());
        System.out.println(schoolBiz);
        System.out.println("================ 垂直业务例子1，只有独立的垂直业务 ===============");
        String communitBiz = verticalBizDemo.getCurrentBiz(BusinessTypeEnums.COMMUNITY.getCode());
        System.out.println(communitBiz);
    }

    @Test
    void getTransportTypeWithoutStrategy() {
        String schoolCourier = verticalBizDemo.getTransportTypeWithoutStrategy(BusinessTypeEnums.SCHOOL.getCode(), TransTypeEnums.COURIER.getCode());
        System.out.println("校园业务，小件员运力：" + schoolCourier);
        String schoolCar = verticalBizDemo.getTransportTypeWithoutStrategy(BusinessTypeEnums.SCHOOL.getCode(), TransTypeEnums.CAR.getCode());
        System.out.println("校园业务，无人车运力：" + schoolCar);
        System.out.println("================ 垂直业务例子2，只有独立的水平模板，需要垂直模板进行订阅，和垂直业务无关 ===============");
        String communityCourier = verticalBizDemo.getTransportTypeWithoutStrategy(BusinessTypeEnums.COMMUNITY.getCode(), TransTypeEnums.COURIER.getCode());
        System.out.println("社区业务，小件员运力：" + communityCourier);
        String communityCar = verticalBizDemo.getTransportTypeWithoutStrategy(BusinessTypeEnums.COMMUNITY.getCode(), TransTypeEnums.CAR.getCode());
        System.out.println("社区业务，无人车运力：" + communityCar);
    }

    @Test
    void getPayTypeWithBusinessType() {
        String schoolNoPay = verticalBizDemo.getPayTypeWithBusinessType(BusinessTypeEnums.SCHOOL.getCode(), PayTypeEnums.NO_PAY.getCode());
        System.out.println("校园业务，无需支付：" + schoolNoPay);
        String schoolNeedPay = verticalBizDemo.getPayTypeWithBusinessType(BusinessTypeEnums.SCHOOL.getCode(), PayTypeEnums.NEED_PAY.getCode());
        System.out.println("校园业务，需要支付：" + schoolNeedPay);
        System.out.println("================ 垂直业务例子3，水平模板和垂直模板一起使用，垂直模板进行订阅 ===============");
        String communityNoPay = verticalBizDemo.getPayTypeWithBusinessType(BusinessTypeEnums.COMMUNITY.getCode(), PayTypeEnums.NO_PAY.getCode());
        System.out.println("社区业务，无需支付：" + communityNoPay);
        // 社区没有订阅需要支付的模板
//        String communityNeedPay = verticalBizDemo.getPayTypeWithBusinessTypessType(BusinessTypeEnums.COMMUNITY.getCode(), PayTypeEnums.NEED_PAY.getCode());
//        System.out.println("社区业务，需要支付：" + communityNeedPay);
    }

    @Test
    void getScenarioVertical() {

        System.out.println("================ 社区预测到站 ===============");
        List<String> communityPredicated  = verticalBizDemo.getScenarioVertical(BusinessTypeEnums.COMMUNITY.getCode(), ScenarioConstant.PREDICATED);
        System.out.println(JSON.toJSONString(communityPredicated));

        System.out.println("================ 社区业务 ===============");
        List<String> community  = verticalBizDemo.getScenarioVertical(BusinessTypeEnums.COMMUNITY.getCode(), null);
        System.out.println(JSON.toJSONString(community));

        System.out.println("================ 校园业务 ===============");
        List<String> school  = verticalBizDemo.getScenarioVertical(BusinessTypeEnums.SCHOOL.getCode(), null);
        System.out.println(JSON.toJSONString(school));

        // 注意，这边虽然没有 校园的预测到站扩展点，但是会返回默认的 校园业务的扩展点，参考代码
        // com.general.extension.support.ExtensionCache.getExtensionPoint，如果取出来为null，则返回 默认的
        System.out.println("================ 校园业务 错误例子 ===============");
        List<String> schoolPredicated  = verticalBizDemo.getScenarioVertical(BusinessTypeEnums.SCHOOL.getCode(), ScenarioConstant.PREDICATED);
        System.out.println(JSON.toJSONString(schoolPredicated));

        System.out.println("================ 垂直业务例子4，新增场景 ===============");

        // 使用例子
        Boolean isMatch = Reducers.reduceByCodes(communityPredicated, CheckProcessExt.class).execute(CheckProcessExt::check, Reducers.allMatch(t -> t));
        System.out.println(isMatch);

    }

    @Test
    void getScenarioHorizontal() {

        System.out.println("================ 水平模板例子，新增场景 ===============");

    }

    @Test
    void testDoubleSession() {

        System.out.println("================ 垂直业务例子4，双层@ExtensionSession测试 ===============");

    }

}
