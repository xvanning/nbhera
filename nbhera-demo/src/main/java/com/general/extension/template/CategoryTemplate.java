package com.general.extension.template;

import com.general.extension.annotation.TemplateConfig;
import com.general.extension.enums.TemplateType;

/**
 * @author xvanning
 * date: 2021/7/17 23:57
 * desc: 注册简单声明的模板类
 */
public class CategoryTemplate {

    // 显示指定为 水平模板
    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String NO_PAY = "NO_PAY";
    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String NEED_PAY = "NEED_PAY";
    //    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String CAR_TRANSPORT = "CAR_TRANSPORT";
    //    @TemplateConfig(type = TemplateType.HORIZONTAL)
    public static final String COURIER_TRANSPORT = "COURIER_TRANSPORT";
}
