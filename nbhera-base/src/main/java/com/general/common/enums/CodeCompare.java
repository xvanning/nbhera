package com.general.common.enums;

import java.util.Objects;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/5/14 0:44
 * desc: 主要用于枚举code与枚举的比较
 */
public interface CodeCompare<T> {

    /**
     * 获取枚举类的code
     *
     * @return 枚举类的code
     */
    T getCode();

    /**
     * 比较两个枚举是否相等
     *
     * @param code code
     * @return true 相等，false 不相等
     */
    default boolean compare(Object code) {
        return Objects.equals(code, getCode());
    }
}
