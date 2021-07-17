package com.general.extension.template;

import com.general.extension.annotation.TemplateConfig;
import com.general.extension.enums.TemplateType;

/**
 * @author xvanning
 * date: 2021/7/17 23:57
 * desc:
 */
public class CategoryTemplate {

    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String NO_PAY = "NO_PAY";
    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String NEED_PAY = "NEED_PAY";
    //    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String CAR_TRANSPORT = "CAR_TRANSPORT";
    //    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String COURIER_TRANSPORT = "COURIER_TRANSPORT";
}
