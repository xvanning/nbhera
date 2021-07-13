package com.general.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/5/29 21:05
 * desc:
 */
@Slf4j
public class SpringApplicationContextHolder implements ApplicationContextAware {

    /**
     * spring上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 获取spring上下文
     *
     * @return spring上下文
     */
    public static ApplicationContext getApplicationContext() {
        return SpringApplicationContextHolder.applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * 根据类型获取所有 Spring 上下文中的bean
     *
     * @param targetClass bean的类型
     * @param <T>         泛型
     * @return key：bean名称， value：spring bean
     */
    public static <T> Map<String, T> getBeanByType(Class<T> targetClass) {
        return applicationContext.getBeansOfType(targetClass);
    }

    /**
     * 根据class尝试去从spring上下文获取bean，优先根据beanClass类型，
     *
     * @param beanClass beanClass类型
     * @param <T>       泛型
     * @return 对应的bean
     */
    public static <T> T getSpringBean(Class<T> beanClass) {
        String beanName = StringUtils.uncapitalize(beanClass.getSimpleName());
        Object bean = null;

        try {
            if (null == applicationContext) {
                log.warn("spring application context is not injected");
                return null;
            }
            bean = applicationContext.getBean(beanClass);
        } catch (BeansException e1) {
            log.warn("spring application context is not injected by class: " + beanClass.getName());
            try {
                bean = applicationContext.getBean(beanName);
            } catch (BeansException e2) {
                log.warn("spring application context is not injected by class: " + beanClass.getName());
            }
        }

        return (T) bean;
    }
}
