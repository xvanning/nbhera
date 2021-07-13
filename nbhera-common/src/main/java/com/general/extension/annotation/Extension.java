package com.general.extension.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/30 20:47
 * desc: 扩展点定义
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extension {

    /**
     * 模板code
     *
     * @return 业务code
     */
    String value() default "";

    /**
     * 场景code
     *
     * @return 场景
     */
    String scenario() default "";

    /**
     * 扩展点优先级
     * 1、如果不是自由扩展点，当前值等于模板优先级
     * 2、如果当前值不是2000，则充值当前值
     *
     * @return 扩展点的优先级
     */
    int priority() default 2000;

    /**
     * 是否是默认扩展点
     *
     * @return 是否是默认扩展点
     */
    boolean isDefault() default false;
}
