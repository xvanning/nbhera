package com.general.common.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/6/20 15:08
 * desc: 注解参数
 */
@Getter
@AllArgsConstructor
public class AnnotationParam {

    /**
     * 参数索引
     */
    private int index;

    /**
     * 是否下探
     */
    private boolean deep;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    private Class<?> type;

    /**
     * 当前字段不需要下探时注解信息
     */
    private Annotation annotation;

    /**
     * 如果需要下探，下探注解对象
     */
    private List<AnnotationFieldParam> fields;

    /**
     * 构造方法，针对不需要下探的情况
     *
     * @param index      当前索引
     * @param type       字段类型
     * @param annotation 注解
     * @param name       字段名称
     */
    public AnnotationParam(int index, Class<?> type, Annotation annotation, String name) {
        Objects.requireNonNull(annotation);
        this.index = index;
        this.type = type;
        this.name = name;
        this.deep = false;
        this.annotation = annotation;
    }

    /**
     * 构造方法，针对需要下探的情况
     *
     * @param index  当前索引
     * @param type   字段类型
     * @param fields 字段解析对象
     */
    public AnnotationParam(int index, Class<?> type, List<AnnotationFieldParam> fields) {
        this.index = index;
        this.type = type;
        this.deep = true;
        this.fields = fields;
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
