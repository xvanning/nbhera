package com.general.common.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/16 22:42
 * desc: 参数不能为空校验
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {

    /**
     * 校验不通过时错误信息
     *
     * @return 错误信息
     */
    String value() default "参数不能为空";
}
