package com.general.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author xvanning
 * date: 2021/6/23 22:55
 * desc: 枚举例子
 */
@AllArgsConstructor
@Getter
public enum DemoEnum implements CodeCompare<Integer> {
    FIRST(1, "1");


    /**
     * code
     */
    private Integer code;

    /**
     * 描述
     */
    private String desc;

    /**
     * 根据code获取枚举
     *
     * @param code code
     * @return 枚举
     */
    public static DemoEnum getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (DemoEnum demoEnum : values()) {
            if (demoEnum.getCode().equals(code)) {
                return demoEnum;
            }
        }
        return null;
    }

    /**
     * 根据code获取枚举
     *
     * @param code code
     * @return 枚举
     */
    public static DemoEnum valueOf(Integer code) {
        return Arrays.stream(values()).filter(v -> v.getCode().equals(code)).findFirst().orElse(null);
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
