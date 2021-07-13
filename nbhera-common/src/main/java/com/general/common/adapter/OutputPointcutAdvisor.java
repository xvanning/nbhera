package com.general.common.adapter;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * @author xvanning
 * date: 2021/6/22 0:14
 * desc: 创建切面
 */
public class OutputPointcutAdvisor extends DefaultPointcutAdvisor {

    /**
     * 构造函数
     *
     * @param pointcut 切点
     * @param advice   拦截器
     */
    public OutputPointcutAdvisor(Pointcut pointcut, Advice advice) {
        super(pointcut, advice);
    }

    /**
     * 创建切面
     *
     * @param expression 切点表达式
     * @return 切面
     */
    public static OutputPointcutAdvisor of(String expression) {
        return of(expression, null);
    }

    /**
     * 构造切面
     *
     * @param expression   表达式
     * @param outputLogger 日志输出类
     * @return 切面
     */
    private static OutputPointcutAdvisor of(String expression, OutputLogger outputLogger) {
        // 设置需要拦截的方法 - 用切点语言来写
        AspectJExpressionPointcut cut = new AspectJExpressionPointcut();
        cut.setExpression(expression);

        // 设置处理
        OutputMethodInterceptor interceptor = null == outputLogger ? new OutputMethodInterceptor() : new OutputMethodInterceptor(outputLogger);

        return new OutputPointcutAdvisor(cut, interceptor);
    }
}
