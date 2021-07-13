package com.general.test.annotation;

import com.general.test.ExpressionTestContextAdapter;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/7/1 0:35
 * desc: 测试方法注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NbTest {

    /**
     * 命中测试规则后，默认的返回值
     *
     * @return 默认的返回值
     */
    String defReturn() default "";

    /**
     * 返回测试规则命名空间
     *
     * @return 返回测试规则命名空间
     */
    String namespace() default ExpressionTestContextAdapter.DEFAULT;

    /**
     * 白名单规则表达式，当命中当前表达式，则不进行代理处理
     *
     * @return 白名单规则
     */
    String expression() default "";
}
