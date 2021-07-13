package com.general.integration.support;

import com.general.integration.annotation.NbClientLog;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author xvanning
 * date: 2021/6/23 23:52
 * desc: 三方接口日志切面定义
 */
public class IntegrationPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    /**
     * 构造函数
     */
    public IntegrationPointcutAdvisor() {
        this.setAdvice(new IntegrationMethodInterceptor());
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return AnnotationUtils.findAnnotation(method, NbClientLog.class) != null;
    }
}
