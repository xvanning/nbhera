package com.general.extension;

import com.general.extension.annotation.ExtensionSession;
import com.general.extension.biz.RequestForTest;
import com.general.extension.biz.horizontal.paytype.PayTypeNameExt;
import com.general.extension.biz.horizontal.transport.TransportNameExt;
import com.general.extension.biz.vertical.simple.PostProcessorExt;
import com.general.extension.scenario.checkrules.CheckRulesExt;
import com.general.extension.scenario.horizontal.RealTransportName;
import com.general.extension.session.FirstSessionExt;
import com.general.extension.strategy.demo1.PackTypeExt;
import com.general.extension.strategy.demo2.PackTypeWithHorizontalExt;
import com.general.nbhera.extension.aonnotation.BizParam;
import com.general.nbhera.extension.aonnotation.Scenario;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xvanning
 * date: 2021/7/17 17:00
 * desc:
 */
@Component
public class ExtensionTestDemo {
    @Resource
    PackTypeExt packTypeExt;
    @Resource
    PackTypeWithHorizontalExt packTypeWithHorizontalExt;
    @Resource
    PostProcessorExt postProcessorExt;
    @Resource
    TransportNameExt transportNameExt;
    @Resource
    PayTypeNameExt payTypeNameExt;
    @Resource
    CheckRulesExt checkRulesExt;
    @Resource
    RealTransportName realTransportName;
    @Resource
    FirstSessionExt firstSessionExt;
    @Resource
    DoubleSessionDemo doubleSessionDemo;

    /**
     * 策略模式例子，需要接口加 @Strategy
     * 根据包裹类型，走不同的子类实现
     * 这边的 @Extension 不与任何业务模板绑定
     *
     * @param packType 包裹类型
     * @return 包裹类型描述
     */
    public String getPackTypeByStrategy(Integer packType) {
        return packTypeExt.reduce(packType).getPackType();
    }

    /**
     * 策略模式例子，需要接口加 @Strategy
     * 根据 水平模板 的类型，走不同的子类实现
     * 这边的 @Extension 仅仅与 水平模板 code 绑定
     *
     * @param transportType 运力类型
     * @return 包裹类型描述
     */
    public String getPackTypeByStrategyWithHorizontalExt(Integer transportType) {
        return packTypeWithHorizontalExt.reduce(transportType).getPackType();
    }

    /**
     * 垂直业务例子
     * 仅仅只有 【垂直业务】 的例子
     * 根据 垂直业务类型，走 不同的垂直业务子类实现
     *
     * @param businessType 业务类型
     * @return 业务类型描述
     */
    @ExtensionSession
    public String getCurrentBiz(@BizParam("businessType") Integer businessType) {
        // 注意，这边不需要reduce，直接根据 businessType 走不同的实现
        return postProcessorExt.getCurrentBiz();
    }

    /**
     * 垂直业务例子
     * 【垂直业务 + 订阅模板】 的例子
     * 根据 【垂直业务】 订阅了 车和人的水平模板，然后根据reduce，走不同的垂直业务子类实现
     * 这里只是垂直业务进行了订阅，实际中没有使用到 businessType
     * 只用到了 transportType
     * {@link com.general.extension.template.TemplateDemo}
     *
     * @param businessType  业务类型
     * @param transportType 运力类型
     * @return 业务类型描述
     */
    @ExtensionSession
    public String getTransportTypeWithoutStrategy(@BizParam("businessType") Integer businessType, Integer transportType) {
        return transportNameExt.reduce(transportType).getTransportName();
    }


    /**
     * 垂直业务例子
     * 【垂直业务 + 订阅模板】 的例子，双层复杂实现
     *
     * @param businessType 业务类型
     * @param payType      支付类型
     * @return 业务类型描述
     */
    @ExtensionSession
    public String getPayTypeWithBusinessType(@BizParam("businessType") Integer businessType, Integer payType) {
        return payTypeNameExt.reduce(payType).getPayTypeNameWithBusiness();
    }

    /**
     * 策略模式不支持 场景，没有入参可以传递进去
     * 垂直业务的场景例子
     *
     * @param businessType 业务类型
     * @param scenario     场景值
     * @return 业务类型描述
     */
    @ExtensionSession
    public List<String> getScenarioVertical(@BizParam("businessType") Integer businessType, @Scenario String scenario) {
        return checkRulesExt.getCheckRulesList();
    }

    /**
     * 策略模式不支持 场景，没有入参可以传递进去
     * 垂直业务的场景例子
     *
     * @param requestForTest req
     * @return 业务类型描述
     */
    @ExtensionSession
    public String getScenarioHorizontal(RequestForTest requestForTest) {
        return realTransportName.reduce(requestForTest.getTransType()).getRealTransportName();
    }

    /**
     * 双层 @ExtensionSession
     * 垂直业务的场景例子
     *
     * @param businessType businessType
     * @return 业务类型描述
     */
    @ExtensionSession
    public String getDoubleSessionOnlyBiz(@BizParam("businessType") Integer businessType, Integer businessType2) {
        String firstSession = firstSessionExt.getFirstSession();
        String secondSession = doubleSessionDemo.getDoubleSession(businessType2);
        return firstSession + " ======== " + secondSession;
    }

    /**
     * 双层 @ExtensionSession
     * 垂直业务的场景例子
     *
     * @param businessType businessType
     * @return 业务类型描述
     */
    @ExtensionSession
    public String getDoubleSessionWithHorizontalExt(@BizParam("businessType") Integer businessType, Integer payType, Integer businessType2, Integer payType2) {
        String firstSession = payTypeNameExt.reduce(payType).getPayTypeNameWithBusiness();
        System.out.println(firstSession);
        String secondSession = doubleSessionDemo.getDoubleSessionWithHorizontalExt(businessType2, payType2);
        System.out.println(secondSession);
        return firstSession + " ======== " + secondSession;
    }
}
