package com.general.common.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/16 21:15
 * desc: 字符长度
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Length {

    /**
     * 最小长度
     *
     * @return 最小长度
     */
    int min() default 0;

    /**
     * 最大长度
     *
     * @return 最大长度
     */
    int max() default Integer.MAX_VALUE;
}
