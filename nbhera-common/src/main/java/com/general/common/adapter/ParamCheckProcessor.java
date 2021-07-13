package com.general.common.adapter;

import java.lang.annotation.Annotation;

/**
 * @param <T>     注解泛型
 * @param <Value> 值泛型
 * @author xvanning
 * date: 2021/6/21 23:46
 * desc: 参数校验处理器
 */
public interface ParamCheckProcessor<T extends Annotation, Value> {
    /**
     * 是否通过校验
     *
     * @param annotation 当前字段带有的注解
     * @param valueClass 值类型
     * @param value      当前字段值
     * @return 是否通过校验
     */
    boolean check(T annotation, Class<?> valueClass, Value value);

    /**
     * 如果不通过，返回的错误信息
     *
     * @param annotation 当前字段带有的注解
     * @param value      当前字段值
     * @return 错误信息
     */
    String getMessage(T annotation, Value value);
}
