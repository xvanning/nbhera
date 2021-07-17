package com.general.extension.boot;

import com.general.common.bean.AnnotationMethod;
import com.general.extension.annotation.ExtensionSession;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author xvanning
 * date: 2021/5/30 21:32
 * desc: 实现类切面，将带@ExtensnionSession的注解进行扫描解析
 */
public class SessionInstancePointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    /**
     * 创建切面
     */
    public SessionInstancePointcutAdvisor() {
        this.setAdvice(new SessionServiceMethodInterceptor());
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        boolean match = AnnotationUtils.findAnnotation(method, ExtensionSession.class) != null;
        // 在启动的时候就生成对应的方法注解缓存
        if (match) {
            SessionServiceMethodInterceptor.ANNOTATION_MAP_CACHE.computeIfAbsent(method, value -> AnnotationMethod.of(method, SessionServiceMethodInterceptor.CHECK_ANNOTATION));
        }
        return match;
    }
}
