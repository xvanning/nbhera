package com.general.common.exception;

/**
 * @author xvanning
 * date: 2021/5/16 22:50
 * desc: 异常码
 */
public interface ResultCode {

    /**
     * 参数异常
     */
    int PARAM = 0;

    /**
     * 业务异常
     */
    int BUSINESS = 1;

    /**
     * 系统异常
     */
    int SYSTEM = 2;

    /**
     * 三方调用异常
     */
    int THIRD = 3;

    /**
     * 获取异常码
     *
     * @return 异常码
     */
    String getCode();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    String getMessage();

    /**
     * 获取异常类型
     *
     * @return 异常类型
     */
    int getType();


}
