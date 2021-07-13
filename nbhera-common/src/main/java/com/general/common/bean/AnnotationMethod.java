package com.general.common.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * @author xvanning
 * date: 2021/6/20 14:40
 * desc: 方法注解信息类
 */
public class AnnotationMethod {

    /**
     * 需要解析的注解类型集合
     */
    private List<Class<? extends Annotation>> annotations;

    /**
     * 当前方法
     */
    @Getter
    private Method method;

    /**
     * 包含的注解
     */
    @Getter
    private Map<Class<? extends Annotation>, List<AnnotationParam>> annotationMap = Maps.newConcurrentMap();

    /**
     * 返回注解解析对象
     *
     * @param method      方法
     * @param annotations 注解
     * @return 注解方法解析对象
     */
    public static AnnotationMethod of(Method method, List<Class<? extends Annotation>> annotations) {
        AnnotationMethod annotationMethod = new AnnotationMethod();
        annotationMethod.annotations = annotations;
        annotationMethod.method = method;
        annotationMethod.build(method.getParameters());
        return annotationMethod;
    }

    /**
     * 构造方法
     *
     * @param parameters 方法参数
     */
    private void build(Parameter[] parameters) {
        if (null == parameters || parameters.length == 0) {
            return;
        }
        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            Integer i = index;

            // 尝试区解析对象
            this.annotations.forEach(annotationClass -> {
                AnnotationParam annotationParam = resolve(annotationClass, parameter, i);
                if (annotationParam != null) {
                    List<AnnotationParam> annotationParams = annotationMap.computeIfAbsent(annotationClass, value -> Lists.newArrayList());
                    annotationParams.add(annotationParam);
                }
            });
        }
    }

    /**
     * 根据注解类型，返回注解所在类位置列表
     *
     * @param annotationClass 注解类
     * @return 注解所在类位置列表
     */
    public List<AnnotationParam> get(Class<?> annotationClass) {
        return annotationMap.get(annotationClass);
    }

    /**
     * 判断当前对象解析注解对象是否为null
     *
     * @return 是否是null
     */
    public boolean isEmpty() {
        return annotationMap.isEmpty();
    }

    /**
     * 是否包含当前注解
     *
     * @param annotationClass 注解类型
     * @param <T>             泛型
     * @return 是否包含
     */
    public <T extends Annotation> Boolean contains(Class<T> annotationClass) {
        return annotationMap.containsKey(annotationClass);
    }

    /**
     * 根据参数和目标注解类，判断当前参数是否含有注解，并且是否需要下探
     *
     * @param annotationClass 目标注解类
     * @param parameter       参数属性
     * @param index           当前索引
     * @param <T>             泛型
     * @return 注解
     */
    private <T extends Annotation> AnnotationParam resolve(Class<T> annotationClass, Parameter parameter, Integer index) {
        T target = parameter.getAnnotation(annotationClass);
        if (null != target) {
            return new AnnotationParam(index, parameter.getType(), target, parameter.getName());
        }
        // 如果当前不是基础类，则下探第一层数据
        if (!GlobalConfig.isNative(parameter.getType())) {
            Field[] fields = parameter.getType().getDeclaredFields();
            if (null != fields && fields.length > 0) {
                List<AnnotationFieldParam> fieldParams = Lists.newArrayList();
                for (Field field : fields) {
                    AnnotationFieldParam annotationFieldParam = AnnotationFieldParam.of(parameter.getType(), field, annotationClass);
                    if (null != annotationFieldParam) {
                        fieldParams.add(annotationFieldParam);
                    }
                }
                if (CollectionUtils.isNotEmpty(fieldParams)) {
                    return new AnnotationParam(index, parameter.getType(), fieldParams);
                }
            }
        }
        return null;
    }
}
