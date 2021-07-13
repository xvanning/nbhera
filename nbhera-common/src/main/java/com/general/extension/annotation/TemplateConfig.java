package com.general.extension.annotation;

import com.general.extension.enums.TemplateType;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/30 20:48
 * desc: 模板声明
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TemplateConfig {

    /**
     * 模板code
     *
     * @return 模板code
     */
    String value() default "";

    /**
     * 优先级越小越靠前，默认值为-1， 使用模板类型优先级
     *
     * @return 优先级
     */
    int priority() default -1;

    /**
     * 模板类型，默认值为垂直业务模板
     *
     * @return 模板类型
     */
    TemplateType type() default TemplateType.VERTICAL;

    /**
     * 设置订阅模板
     *
     * @return 订阅的模板
     */
    String[] subscribe() default "";
}
