package com.general.common.util;

import com.general.common.enums.CodeCompare;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xvanning
 * date: 2021/5/16 22:08
 * desc: 枚举工具类
 */
public class EnumUtils {

    /**
     * 枚举缓存
     */
    private static final Map<Class<?>, EnumSet<? extends Enum<?>>> ENUM_SET_MAP = new ConcurrentHashMap<>(256);

    /**
     * 根据 code 判断是否在指定枚举之中， 枚举类必须实现CodeCompare
     *
     * @param targetClass 枚举目标类
     * @param code        code
     * @return 是否在枚举中0
     */
    public static boolean isEnum(Class<?> targetClass, Object code) {
        return getEnumByCode(targetClass, code) != null;
    }

    /**
     * 根据code获取对应的枚举值
     *
     * @param targetClass 枚举泛型
     * @param code        枚举目标类
     * @param <E>         泛型
     * @return 枚举
     */
    private static <E extends Enum<E>> E getEnumByCode(Class targetClass, Object code) {
        if (null == targetClass || !CodeCompare.class.isAssignableFrom(targetClass)) {
            return null;
        }
        // 把当前枚举类的所有实例装到缓存里
        EnumSet<E> result = (EnumSet<E>) ENUM_SET_MAP.computeIfAbsent(targetClass, value -> EnumSet.allOf(targetClass));
        if (result.isEmpty()) {
            return null;
        }

        // 遍历获取到的缓存
        for (E e : result) {
            if (((CodeCompare) e).compare(code)) {
                return e;
            }
        }
        return null;
    }
}
