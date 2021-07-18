package com.general.extension.template;

import com.general.extension.annotation.TemplateConfig;
import com.general.extension.enums.TemplateType;

import static com.general.extension.template.CategoryTemplate.*;

/**
 * @author xvanning
 * date: 2021/7/17 17:12
 * desc: 模板配置
 */
public class TemplateDemo {

    /**
     * 订阅的 code 需要显示指定为 水平模板
     * 或者 有统一的类进行继承，表明为 水平模板{@link com.general.extension.config.CarTemplate}
     * 接受订阅的类需要是 垂直模板
     */
    @TemplateConfig(subscribe = {NO_PAY, NEED_PAY, CAR_TRANSPORT, COURIER_TRANSPORT}, type = TemplateType.VERTICAL)
    public static final String SCHOOL_BIZ = "SCHOOL_BIZ";

    @TemplateConfig(subscribe = {NO_PAY, CAR_TRANSPORT, COURIER_TRANSPORT})
    public static final String COMMUNITY_BIZ = "COMMUNITY_BIZ";


}
