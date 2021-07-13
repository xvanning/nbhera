package com.general.integration.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/6/23 23:00
 * desc: 三方接口的日志记录
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NbClientLog {

    /**
     * 命中测试规则后，默认返回值
     *
     * @return 默认返回值
     */
    String defaultRet() default "";

    /**
     * 是否需要抛出异常
     *
     * @return 默认为true
     */
    boolean needThrow() default true;

    /**
     * 允许的超时时间
     *
     * @return 默认2000ms
     */
    int timeout() default -1;
}
