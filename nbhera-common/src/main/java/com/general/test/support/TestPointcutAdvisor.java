package com.general.test.support;

import com.general.test.annotation.NbTest;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author xvanning
 * date: 2021/7/1 0:54
 * desc: 测试接口日志切面定义
 */
public class TestPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {
    public TestPointcutAdvisor() {
        this.setAdvice(new TestMethodInterceptor());
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return AnnotationUtils.findAnnotation(method, NbTest.class) != null;
    }
}
