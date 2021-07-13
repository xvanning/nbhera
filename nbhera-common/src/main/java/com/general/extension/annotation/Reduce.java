package com.general.extension.annotation;

import com.general.extension.enums.ReduceType;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/30 20:47
 * desc: 扩展点决策策略补充
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reduce {

    /**
     * 返回决策类型，默认按优先级选择第一个
     *
     * @return 决策类型
     */
    ReduceType value();
}
