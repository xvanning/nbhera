package com.general.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xvanning
 * date: 2021/7/24 20:49
 * desc: 支付类型枚举
 */
@AllArgsConstructor
@Getter
public enum PayTypeEnums {

    NO_PAY(1, "无需支付"),
    NEED_PAY(2, "需要支付");

    private final int code;

    private final String desc;
}
