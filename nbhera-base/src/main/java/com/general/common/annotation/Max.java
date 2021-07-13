package com.general.common.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/16 21:18
 * desc: 检查被标注的属性的值是否是小于等于给定的值
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Max {

    /**
     * 限定的最大值
     *
     * @return 最大值
     */
    int value();
}
