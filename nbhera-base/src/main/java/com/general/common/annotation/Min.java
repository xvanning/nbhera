package com.general.common.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/16 21:18
 * desc: 检查被标注的属性的值是否是大于等于给定的值
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Min {

    /**
     * 限定的最小值
     *
     * @return 最小值
     */
    int value();
}
