package com.general.common.enums;

import com.general.common.exception.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xvanning
 * date: 2021/6/23 22:48
 * desc: 系统错误码
 */
@AllArgsConstructor
@Getter
public enum ErrorCode implements ResultCode {

    /**
     * 参数错误
     */
    PARAM_ERROR(PARAM, "PARAM_ERROR", "参数错误"),

    /**
     * 业务错误
     */
    BUSINESS_ERROR(BUSINESS, "BUSINESS_ERROR", "业务错误"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR(BUSINESS, "SYSTEM_ERROR", "系统错误"),

    /**
     * 系统错误
     */
    THIRD_ERROR(BUSINESS, "THIRD_ERROR", "三方错误");


    /**
     * 类型
     */
    private int type;

    /**
     * 错误码code
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getType() {
        return this.type;
    }
}
