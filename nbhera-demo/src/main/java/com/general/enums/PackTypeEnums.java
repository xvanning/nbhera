package com.general.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xvanning
 * date: 2021/7/24 20:24
 * desc: 包裹类型枚举
 */
@AllArgsConstructor
@Getter
public enum PackTypeEnums {

    SMALL(1, "小件"),
    BIG(2, "大件");

    private final int code;

    private final String desc;
}
