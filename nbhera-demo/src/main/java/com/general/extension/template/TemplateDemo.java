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

    @TemplateConfig(subscribe = {NO_PAY, NEED_PAY, CAR_TRANSPORT, COURIER_TRANSPORT})
    public static final String SCHOOL_BIZ = "SCHOOL_BIZ";

    @TemplateConfig(subscribe = {NO_PAY, CAR_TRANSPORT, COURIER_TRANSPORT})
    public static final String COMMUNITY_BIZ = "COMMUNITY_BIZ";


}
