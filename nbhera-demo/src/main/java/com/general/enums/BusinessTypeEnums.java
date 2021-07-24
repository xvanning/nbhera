package com.general.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xvanning
 * date: 2021/7/24 20:17
 * desc: 业务类型枚举
 */
@Getter
@AllArgsConstructor
public enum BusinessTypeEnums {

    SCHOOL(1, "校园业务"),
    COMMUNITY(2, "社区业务");

    private final int code;

    private final String desc;
}
