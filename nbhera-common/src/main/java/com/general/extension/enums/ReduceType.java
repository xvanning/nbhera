package com.general.extension.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xvanning
 * date: 2021/5/30 21:00
 * desc: 决策类型
 */
@AllArgsConstructor
@Getter
public enum ReduceType {

    NONE(false, "执行时返回所有扩展点结果"),
    FIRST(true, "执行时返回排序第一个"),
    ANY_MATCH(true, "如果有一个结果返回true，则返回true"),
    MAP(true, "会把多个扩展点返回的map合并成一个"),
    LIST(true, "会把多个扩展点返回的collection合并成一个"),
    ALL_MATCH(true, "是否所有元素都满足true，如果所有元素都为空，则返回false");

    /**
     * 支持在代理对象中调用
     */
    private Boolean support;

    /**
     * 方法描述
     */
    private String desc;
}
