package com.general.extension.boot;

import com.general.common.util.SpringApplicationContextHolder;
import com.general.extension.support.ExtensionBoot;
import com.general.extension.support.ExtensionClassSet;
import com.general.extension.support.ExtensionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

/**
 * @author xvanning
 * date: 2021/5/30 21:32
 * desc: 扩展点 bean 注册器
 */
@Slf4j
public class ExtensionImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String SESSION_CREATE_AOP = "SessionScopeCreateAspect";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        // 查询配置
        AnnotationAttributes annAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(ExtensionConfiguration.class.getName()));
        String[] scanPackages = annAttrs.getStringArray("value");
        log.info("======================== [start] ========================");
        log.info("@ExtensionConfiguration begin start! scanPackage = {}", Arrays.toString(scanPackages));

        // 类扫描
        ExtensionClassSet extensionClassSet = ExtensionClassSet.of(scanPackages);
        log.info(extensionClassSet.toString());

        // 设置spring上下文类
        beanDefinitionRegistry.registerBeanDefinition(StringUtils.uncapitalize(SpringApplicationContextHolder.class.getSimpleName()), buildGeneralBeanDefinition(SpringApplicationContextHolder.class));

        // session切面注册
        prepareSessionScopeCreateAspect(beanDefinitionRegistry);

        // 扩展点启动器
        prepareExtensionBoot(beanDefinitionRegistry, scanPackages, extensionClassSet);

        // 扫描路径输出
        extensionClassSet.getExtensionPointClasses().forEach(extensionPoint -> {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ExtensionFactoryBean.class);
            beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
            beanDefinitionBuilder.setLazyInit(false);
            beanDefinitionBuilder.addConstructorArgValue(extensionPoint);
            beanDefinitionRegistry.registerBeanDefinition(StringUtils.uncapitalize(extensionPoint.getSimpleName()), beanDefinitionBuilder.getBeanDefinition());
            log.info("register extension beanName = {}, beanClass = {}", StringUtils.uncapitalize(extensionPoint.getSimpleName()), extensionPoint);
        });


    }

    /**
     * 注册spring extension boot
     *
     * @param registry          注册器
     * @param scanPackages      扫描路径
     * @param extensionClassSet 扩展点扫描类
     */
    private void prepareExtensionBoot(BeanDefinitionRegistry registry, String[] scanPackages, ExtensionClassSet extensionClassSet) {
        log.info("prepare ExtensionBoot.class");
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ExtensionBoot.class);
        beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinitionBuilder.setLazyInit(false);
        beanDefinitionBuilder.addConstructorArgValue(scanPackages);
        beanDefinitionBuilder.addConstructorArgValue(extensionClassSet);
        beanDefinitionBuilder.setInitMethodName("init");
        registry.registerBeanDefinition("extensionBoot", beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 注册session aop
     *
     * @param registry 注册器
     */
    private void prepareSessionScopeCreateAspect(BeanDefinitionRegistry registry) {
        log.info("prepare SessionInstancePointcutAdvisor.class");
        registry.registerBeanDefinition(SESSION_CREATE_AOP, buildGeneralBeanDefinition(SessionInstancePointcutAdvisor.class));
    }

    /**
     * 定义单例bean
     *
     * @param clazz 目标类
     * @return bean定义
     */
    private BeanDefinition buildGeneralBeanDefinition(Class<?> clazz) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinitionBuilder.setLazyInit(false);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        beanDefinition.setAutowireCandidate(true);
        return beanDefinition;
    }

    /**
     * 注册bean到spring上下文
     *
     * @param targetClass            目标class
     * @param beanDefinitionRegistry bean定义注册
     * @param constructorArgValues   构造参数
     */
    private void register(Class<?> targetClass, BeanDefinitionRegistry beanDefinitionRegistry, Object... constructorArgValues) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(targetClass);
        beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinitionBuilder.setLazyInit(false);
        // 构造方法注入依赖参数
        if (null != constructorArgValues) {
            for (Object constructorArgValue : constructorArgValues) {
                beanDefinitionBuilder.addConstructorArgValue(constructorArgValue);
            }
        }
        beanDefinitionRegistry.registerBeanDefinition(StringUtils.uncapitalize(targetClass.getSimpleName()), beanDefinitionBuilder.getBeanDefinition());
    }
}
