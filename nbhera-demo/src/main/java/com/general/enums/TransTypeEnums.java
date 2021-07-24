package com.general.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xvanning
 * date: 2021/7/24 20:18
 * desc: 运力类型枚举
 */
@Getter
@AllArgsConstructor
public enum TransTypeEnums {

    COURIER(1, "小件员"),
    CAR(2, "无人车");

    private final int code;

    private final String desc;
}
