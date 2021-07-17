package com.general.extension.template;

import com.general.extension.annotation.TemplateConfig;
import com.general.extension.enums.TemplateType;

/**
 * @author xvanning
 * date: 2021/7/17 17:12
 * desc: 模板配置
 */
public class TemplateDemo {

    //    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String NO_PAY = "NO_PAY";
    //    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String NEED_PAY = "NEED_PAY";
    //    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String CAR_TRANSPORT = "CAR_TRANSPORT";
    //    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String COURIER_TRANSPORT = "COURIER_TRANSPORT";






    @TemplateConfig(subscribe = {NO_PAY, NEED_PAY, CAR_TRANSPORT, COURIER_TRANSPORT})
    public static final String SCHOOL_BIZ = "SCHOOL_BIZ";

    @TemplateConfig(subscribe = {NO_PAY, CAR_TRANSPORT, COURIER_TRANSPORT})
    public static final String COMMUNITY_BIZ = "COMMUNITY_BIZ";


}
