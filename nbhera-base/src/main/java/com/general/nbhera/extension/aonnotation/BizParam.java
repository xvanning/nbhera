package com.general.nbhera.extension.aonnotation;

import java.lang.annotation.*;

/**
 * @author general
 * date: 2021/5/13 23:49
 * desc: 扩展点业务身份解析字段
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizParam {

    /**
     * 扩展点业务身份解析字段对应key，如果没有设置，则默认使用字段名称
     *
     * @return key 身份解析维度字段
     */
    String value() default "";

}
