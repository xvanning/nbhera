package com.general.common.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/16 21:13
 * desc: 是否为指定枚举
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssertEnum {

    /**
     * 执行枚举类型
     *
     * @return 枚举类型
     */
    Class<? extends Enum<?>> value();

    /**
     * 校验不通过时返回
     *
     * @return 错误信息
     */
    String message() default "未知的参数";
}
