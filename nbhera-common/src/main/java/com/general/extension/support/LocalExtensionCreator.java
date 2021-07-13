package com.general.extension.support;

import com.general.common.util.AssertUtils;
import com.general.common.util.SpringApplicationContextHolder;
import com.general.constants.SystemCode;
import com.general.extension.annotation.Extension;
import com.general.nbhera.extension.ExtensionPoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;


/**
 * @author xvanning
 * date: 2021/6/2 23:02
 * desc: 本地扩展点创建者
 */
@Slf4j
public class LocalExtensionCreator implements ExtensionCreator<Class<? extends ExtensionPoints>> {


    @Override
    public Boolean support(Object param) {
        return null != param || param.getClass() != Class.class || AnnotationUtils.findAnnotation((Class<?>) param, Extension.class) != null;
    }

    @Override
    public ExtensionDefinition create(Class<? extends ExtensionPoints> targetClass) {
        Extension extension = AnnotationUtils.findAnnotation(targetClass, Extension.class);
        AssertUtils.isNotNull(extension, SystemCode.EXTENSION, targetClass + "无法找到@Extension注解");
        return ExtensionDefinition.of(extension.value(), extension.scenario(), ExtensionRegistryCenter.getExtensionInterfaceClass(targetClass), createInstance(targetClass), extension.priority(), extension.isDefault());
    }

    /**
     * 创建扩展点实现
     *
     * @param targetClass 扩展点类
     * @param <T>         泛型
     * @return 扩展点实现
     */
    public <T> T createInstance(Class<T> targetClass) {
        T instance = createBeanInstance(targetClass);
        autoWireBean(instance);
        return instance;
    }

    /**
     * 对bean做装配
     *
     * @param target 目标实例
     */
    public void autoWireBean(Object target) {
        ApplicationContext context = SpringApplicationContextHolder.getApplicationContext();
        if (context != null) {
            try {
                context.getAutowireCapableBeanFactory().autowireBean(target);
            } catch (Throwable throwable) {
                log.error("Failed to autoWireBean " + target.getClass().getName(), throwable);
            }
        }
    }

    /**
     * 根据 class 创建实例
     *
     * @param beanClass beanClass
     * @param <T>       泛型
     * @return 实例
     */
    private <T> T createBeanInstance(Class<T> beanClass) {
        try {
            return beanClass.newInstance();
        } catch (Throwable throwable) {
            log.error("Failed to CreateBeanInstance", throwable);
            return null;
        }
    }
}
