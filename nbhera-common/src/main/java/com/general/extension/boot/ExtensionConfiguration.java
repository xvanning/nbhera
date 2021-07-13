package com.general.extension.boot;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/30 21:28
 * desc: 扩展点 SpringBoot 启动器
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ExtensionImportBeanDefinitionRegistrar.class)
public @interface ExtensionConfiguration {

    /**
     * 扫描的根目录，可以填写多个
     *
     * @return 扫描的根目录
     */
    String[] value() default "";
}
