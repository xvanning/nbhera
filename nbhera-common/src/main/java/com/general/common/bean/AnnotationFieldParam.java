package com.general.common.bean;

import com.general.common.exception.SystemException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author xvanning
 * date: 2021/6/20 15:10
 * desc: 注解解析字段对象
 */
@NoArgsConstructor
public class AnnotationFieldParam {

    /**
     * 当前字段注解信息
     */
    @Getter
    private Annotation annotation;

    /**
     * 字段资源描述
     */
    private PropertyDescriptor targetPds;

    /**
     * 构建字段注解对象
     *
     * @param sourceClass     目标类
     * @param field           字段
     * @param annotationClass 注解class
     * @param <T>             泛型
     * @return 字段注解解析对象
     */
    public static <T extends Annotation> AnnotationFieldParam of(Class sourceClass, Field field, Class<T> annotationClass) {
        Annotation target = field.getAnnotation(annotationClass);
        if (null == target) {
            return null;
        }
        AnnotationFieldParam annotationFieldParam = new AnnotationFieldParam();
        annotationFieldParam.annotation = target;
        annotationFieldParam.targetPds = BeanUtils.getPropertyDescriptor(sourceClass, field.getName());
        return annotationFieldParam;
    }

    /**
     * 根据实例获取当前字段值
     *
     * @param source 实例
     * @return 字段值
     */
    public Object getValue(Object source) {
        Method readMethod = targetPds.getReadMethod();
        try {
            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                readMethod.setAccessible(true);
            }
            return readMethod.invoke(source);
        } catch (Throwable throwable) {
            throw new SystemException("SYSTEM_ERROR", "Could not copy property [" + targetPds.getName() + "] from source to target", throwable);
        }
    }

    /**
     * 获取字段名称
     *
     * @return 字段名称
     */
    public String getName() {
        return targetPds.getName();
    }

    /**
     * 返回字段类型
     *
     * @return 字段类型
     */
    public Class<?> getType() {
        return targetPds.getPropertyType();
    }

    /**
     * 获取注解
     *
     * @param targetClass 注解类型
     * @param <T>         注解泛型
     * @return 注解
     */
    public <T> T getAnnotation(Class<T> targetClass) {
        return (T) annotation;
    }
}
