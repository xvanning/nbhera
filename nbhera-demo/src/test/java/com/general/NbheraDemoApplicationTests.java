package com.general;

import com.alibaba.fastjson.JSON;
import com.general.enums.BusinessTypeEnums;
import com.general.enums.PackTypeEnums;
import com.general.enums.PayTypeEnums;
import com.general.enums.TransTypeEnums;
import com.general.extension.Reducers;
import com.general.extension.biz.RequestForTest;
import com.general.extension.ExtensionTestDemo;
import com.general.extension.scenario.checkrules.rules.CheckProcessExt;
import com.general.extension.template.ScenarioConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class NbheraDemoApplicationTests {
    @Resource
    ExtensionTestDemo extensionTestDemo;

    @Test
    void getPackTypeByStrategy() {
        String smallPack = extensionTestDemo.getPackTypeByStrategy(PackTypeEnums.SMALL.getCode());
        System.out.println(smallPack);
        System.out.println("================ 策略模式例子1，随意任何KEY ===============");
        String bigPack = extensionTestDemo.getPackTypeByStrategy(PackTypeEnums.BIG.getCode());
        System.out.println(bigPack);
    }

    @Test
    void getPackTypeByStrategyWithHorizontalExt() {
        String courierPack = extensionTestDemo.getPackTypeByStrategyWithHorizontalExt(TransTypeEnums.COURIER.getCode());
        System.out.println(courierPack);
        System.out.println("================ 策略模式例子2，直接使用已有的水平模板KEY ===============");
        String carPack = extensionTestDemo.getPackTypeByStrategyWithHorizontalExt(TransTypeEnums.CAR.getCode());
        System.out.println(carPack);
    }

    @Test
    void getCurrentBiz() {
        String schoolBiz = extensionTestDemo.getCurrentBiz(BusinessTypeEnums.SCHOOL.getCode());
        System.out.println(schoolBiz);
        System.out.println("================ 垂直业务例子1，只有独立的垂直业务 ===============");
        String communitBiz = extensionTestDemo.getCurrentBiz(BusinessTypeEnums.COMMUNITY.getCode());
        System.out.println(communitBiz);
    }

    @Test
    void getTransportTypeWithoutStrategy() {
        String schoolCourier = extensionTestDemo.getTransportTypeWithoutStrategy(BusinessTypeEnums.SCHOOL.getCode(), TransTypeEnums.COURIER.getCode());
        System.out.println("校园业务，小件员运力：" + schoolCourier);
        String schoolCar = extensionTestDemo.getTransportTypeWithoutStrategy(BusinessTypeEnums.SCHOOL.getCode(), TransTypeEnums.CAR.getCode());
        System.out.println("校园业务，无人车运力：" + schoolCar);
        System.out.println("================ 垂直业务例子2，只有独立的水平模板，需要垂直模板进行订阅，和垂直业务无关 ===============");
        String communityCourier = extensionTestDemo.getTransportTypeWithoutStrategy(BusinessTypeEnums.COMMUNITY.getCode(), TransTypeEnums.COURIER.getCode());
        System.out.println("社区业务，小件员运力：" + communityCourier);
        String communityCar = extensionTestDemo.getTransportTypeWithoutStrategy(BusinessTypeEnums.COMMUNITY.getCode(), TransTypeEnums.CAR.getCode());
        System.out.println("社区业务，无人车运力：" + communityCar);
    }

    @Test
    void getPayTypeWithBusinessType() {
        String schoolNoPay = extensionTestDemo.getPayTypeWithBusinessType(BusinessTypeEnums.SCHOOL.getCode(), PayTypeEnums.NO_PAY.getCode());
        System.out.println("校园业务，无需支付：" + schoolNoPay);
        String schoolNeedPay = extensionTestDemo.getPayTypeWithBusinessType(BusinessTypeEnums.SCHOOL.getCode(), PayTypeEnums.NEED_PAY.getCode());
        System.out.println("校园业务，需要支付：" + schoolNeedPay);
        System.out.println("================ 垂直业务例子3，水平模板和垂直模板一起使用，垂直模板进行订阅 ===============");
        String communityNoPay = extensionTestDemo.getPayTypeWithBusinessType(BusinessTypeEnums.COMMUNITY.getCode(), PayTypeEnums.NO_PAY.getCode());
        System.out.println("社区业务，无需支付：" + communityNoPay);
        // 社区没有订阅需要支付的模板
//        String communityNeedPay = verticalBizDemo.getPayTypeWithBusinessTypessType(BusinessTypeEnums.COMMUNITY.getCode(), PayTypeEnums.NEED_PAY.getCode());
//        System.out.println("社区业务，需要支付：" + communityNeedPay);
    }

    @Test
    void getScenarioVertical() {

        System.out.println("================ 社区预测到站 ===============");
        List<String> communityPredicated = extensionTestDemo.getScenarioVertical(BusinessTypeEnums.COMMUNITY.getCode(), ScenarioConstant.PREDICATED);
        System.out.println(JSON.toJSONString(communityPredicated));

        System.out.println("================ 社区业务 ===============");
        List<String> community = extensionTestDemo.getScenarioVertical(BusinessTypeEnums.COMMUNITY.getCode(), null);
        System.out.println(JSON.toJSONString(community));

        System.out.println("================ 校园业务 ===============");
        List<String> school = extensionTestDemo.getScenarioVertical(BusinessTypeEnums.SCHOOL.getCode(), null);
        System.out.println(JSON.toJSONString(school));

        // 注意，这边虽然没有 校园的预测到站扩展点，但是会返回默认的 校园业务的扩展点，参考代码
        // com.general.extension.support.ExtensionCache.getExtensionPoint，如果取出来为null，则返回 默认的
        System.out.println("================ 校园业务 错误例子 ===============");
        List<String> schoolPredicated = extensionTestDemo.getScenarioVertical(BusinessTypeEnums.SCHOOL.getCode(), ScenarioConstant.PREDICATED);
        System.out.println(JSON.toJSONString(schoolPredicated));

        System.out.println("================ 垂直业务例子4，新增场景 ===============");

        // 使用例子
        Boolean isMatch = Reducers.reduceByCodes(communityPredicated, CheckProcessExt.class).execute(CheckProcessExt::check, Reducers.allMatch(t -> t));
        System.out.println(isMatch);

    }

    @Test
    void getScenarioHorizontal() {
        // 这里的用法和上面的用法没有本质区别
        // 如果只用到水平模板，还是要加 @ExtensionSession，因为 @Strategy 注解，没法从session中拿到 scenario。
        System.out.println("================ 水平模板例子，新增场景 ===============");
        RequestForTest requestForTest1 = new RequestForTest(BusinessTypeEnums.SCHOOL.getCode(), TransTypeEnums.COURIER.getCode(), null);
        String scenarioHorizontal1 = extensionTestDemo.getScenarioHorizontal(requestForTest1);
        System.out.println(scenarioHorizontal1);
        RequestForTest requestForTest2 = new RequestForTest(BusinessTypeEnums.SCHOOL.getCode(), TransTypeEnums.COURIER.getCode(), ScenarioConstant.BOSS);
        String scenarioHorizontal2 = extensionTestDemo.getScenarioHorizontal(requestForTest2);
        System.out.println(scenarioHorizontal2);
        RequestForTest requestForTest3 = new RequestForTest(BusinessTypeEnums.SCHOOL.getCode(), TransTypeEnums.CAR.getCode(), null);
        String scenarioHorizontal3 = extensionTestDemo.getScenarioHorizontal(requestForTest3);
        System.out.println(scenarioHorizontal3);
        RequestForTest requestForTest4 = new RequestForTest(BusinessTypeEnums.SCHOOL.getCode(), TransTypeEnums.CAR.getCode(), ScenarioConstant.TRUCK);
        String scenarioHorizontal4 = extensionTestDemo.getScenarioHorizontal(requestForTest4);
        System.out.println(scenarioHorizontal4);
    }

    @Test
    void testDoubleSessionOnlyBiz() {
        // 这里是测试 双层@ExtensionSession测试
        String doubleSessionOnlyBiz = extensionTestDemo.getDoubleSessionOnlyBiz(BusinessTypeEnums.SCHOOL.getCode(), BusinessTypeEnums.COMMUNITY.getCode());
        System.out.println(doubleSessionOnlyBiz);
        System.out.println("================ 双层@ExtensionSession测试1 ===============");

    }

    @Test
    void testDoubleSessionWithHorizontalExt() {
        // 这里是测试 双层@ExtensionSession测试
        System.out.println("================ 双层@ExtensionSession测试2, 结合reduce水平模板 ===============");
        String doubleSessionWithHorizontalExt = extensionTestDemo.getDoubleSessionWithHorizontalExt(BusinessTypeEnums.SCHOOL.getCode(), PayTypeEnums.NEED_PAY.getCode(), BusinessTypeEnums.COMMUNITY.getCode(), PayTypeEnums.NO_PAY.getCode());
        System.out.println(doubleSessionWithHorizontalExt);


    }

}
