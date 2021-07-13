package com.general.common.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author xvanning
 * date: 2021/5/26 23:10
 * desc: 泛型工具类
 */
public class GenericUtils {

    /**
     * 获取 class 的第一个接口的第一个泛型
     *
     * @param targetClass 目标类
     * @return 泛型类
     */
    public static Class getGenericClass(Class<?> targetClass) {
        return getGenericClass(targetClass, 0);
    }

    /**
     * 获取 第一个接口 的指定位置的泛型，因为一个接口可以有多个泛型， 所以支持获取指定位置的泛型
     *
     * @param targetClass 目标类
     * @param i           索引位置
     * @return 泛型类
     */
    public static Class getGenericClass(Class<?> targetClass, int i) {
        return getClass(targetClass.getGenericInterfaces()[0], i);
    }

    /**
     * 获取 class 的泛型，因为一个类可以有多个泛型， 所以支持获取指定位置的泛型
     *
     * @param type 目标类
     * @param i    索引位置
     * @return 泛型类
     */
    private static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) {
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0);
        } else {
            return (Class) type;
        }

    }


    /**
     * 获取 class 的指定位置的泛型，因为一个类可以有多个泛型， 所以支持获取指定位置的泛型
     *
     * @param parameterizedType 目标类
     * @param i                 索引位置
     * @return 泛型类
     */
    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) {
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) {
            return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }
}
