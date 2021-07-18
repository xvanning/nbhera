package com.general.extension.biz.vertical;

import com.general.extension.annotation.ExtensionSession;
import com.general.extension.biz.horizontal.transport.TransportNameExt;
import com.general.extension.biz.vertical.simple.PostProcessorExt;
import com.general.extension.strategy.demo1.PackTypeExt;
import com.general.extension.strategy.demo2.PackTypeWithHorizontalExt;
import com.general.nbhera.extension.aonnotation.BizParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xvanning
 * date: 2021/7/17 17:00
 * desc:
 */
@Component
public class VerticalBizDemo {
    @Resource
    PackTypeExt packTypeExt;
    @Resource
    PackTypeWithHorizontalExt packTypeWithHorizontalExt;
    @Resource
    PostProcessorExt postProcessorExt;
    @Resource
    TransportNameExt transportNameExt;

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
     * @param packType 包裹类型
     * @return 包裹类型描述
     */
    public String getPackTypeByStrategyWithHorizontalExt(Integer packType) {
        return packTypeWithHorizontalExt.reduce(packType).getPackType();
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
     * @param businessType 业务类型
     * @return 业务类型描述
     */
    @ExtensionSession
    public String getTransportTypeWithoutStrategy(@BizParam("businessType") Integer businessType, Integer transportType) {
        return transportNameExt.reduce(transportType).getTransportName();
    }


    /**
     * 垂直业务例子
     * 【垂直业务 + 订阅模板】 的例子，双层复杂实现 TODO
     *
     * @param businessType 业务类型
     * @return 业务类型描述
     */
    @ExtensionSession
    public String get(@BizParam("businessType") Integer businessType, Integer transportType) {
        return transportNameExt.reduce(transportType).getTransportName();
    }
}
