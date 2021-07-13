package com.general.common.util;

import com.general.common.exception.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/5/18 0:34
 * desc: 断言工具类，用来辅助抛出业务异常
 * 这里抛出的异常会被aop拦截，统一输出返回
 */
public class AssertUtils {

    /**
     * update 最小影响行数，即为0
     */
    private static final int MIN_COUNT = 0;

    /**
     * 抛出三方异常，不返回请求参数
     * 这里抛出的异常会被aop拦截，统一输出返回
     *
     * @param resultCode 错误码
     * @param response   结果
     */
    public static void throwThirdException(ResultCode resultCode, Object response) {
        throw new ThirdException(resultCode).addExtension("response", response);
    }

    /**
     * 抛出三方异常，返回请求参数
     * 这里抛出的异常会被aop拦截，统一输出返回
     *
     * @param resultCode 错误码
     * @param request    请求
     * @param response   结果
     */
    public static void throwThirdException(ResultCode resultCode, Object request, Object response) {
        throw new ThirdException(resultCode).addExtension("request", request).addExtension("response", response);
    }

    /**
     * 用工具类抛异常
     *
     * @param resultCode resultCode
     */
    public static void throwException(ResultCode resultCode) {
        isTrue(false, resultCode, resultCode.getMessage());
    }

    /**
     * 断言字符串不为空
     *
     * @param charSequence 字符串
     * @param resultCode   错误码
     */
    public static void isNotBlank(CharSequence charSequence, ResultCode resultCode) {
        isNotBlank(charSequence, resultCode, null);
    }

    /**
     * 断言字符串不为空
     *
     * @param charSequence 字符串
     * @param resultCode   错误码
     * @param message      错误信息
     */
    public static void isNotBlank(CharSequence charSequence, ResultCode resultCode, String message) {
        isTrue(StringUtils.isNotBlank(charSequence), resultCode, message);
    }

    /**
     * 两个对象是否相等
     *
     * @param a          a
     * @param b          b
     * @param resultCode 错误码
     */
    public static void isEquals(Object a, Object b, ResultCode resultCode) {
        isTrue(Objects.equals(a, b), resultCode);
    }

    /**
     * 字符串是否是空
     *
     * @param charSequence charSequence
     * @param resultCode   resultCode
     */
    public static void isBlank(CharSequence charSequence, ResultCode resultCode) {
        isBlank(charSequence, resultCode, null);
    }

    /**
     * 字符串是否是空
     *
     * @param charSequence charSequence
     * @param resultCode   resultCode
     * @param message      message
     */
    public static void isBlank(CharSequence charSequence, ResultCode resultCode, String message) {
        isTrue(StringUtils.isBlank(charSequence), resultCode, message);
    }

    /**
     * 对象不为空
     *
     * @param object     object
     * @param resultCode resultCode
     */
    public static void isNotNull(Object object, ResultCode resultCode) {
        isNotNull(object, resultCode, null);
    }

    /**
     * 对象不为空
     *
     * @param object     object
     * @param resultCode resultCode
     * @param message    message
     */
    public static void isNotNull(Object object, ResultCode resultCode, String message) {
        isTrue(null != object, resultCode, message);
    }

    /**
     * 对象不为空
     *
     * @param object    object
     * @param errorCode errorCode
     * @param message   message
     */
    public static void isNotNull(Object object, String errorCode, String message) {
        isTrue(null != object, ResultCode.SYSTEM, errorCode, message);
    }

    /**
     * 对象为空
     *
     * @param object    object
     * @param errorCode errorCode
     * @param message   message
     */
    public static void isNull(Object object, String errorCode, String message) {
        isTrue(null == object, ResultCode.SYSTEM, errorCode, message);
    }

    /**
     * 对象为空
     *
     * @param object     object
     * @param resultCode resultCode
     */
    public static void isNull(Object object, ResultCode resultCode) {
        isTrue(null != object, resultCode, null);
    }

    /**
     * 对象为空
     *
     * @param object     object
     * @param resultCode resultCode
     * @param message    message
     */
    public static void isNull(Object object, ResultCode resultCode, String message) {
        isTrue(null == object, resultCode, message);
    }

    /**
     * 批量校验不为空
     *
     * @param resultCode resultCode
     * @param objects    对象数组
     */
    public static void batchNotNull(ResultCode resultCode, Object... objects) {
        if (objects == null) {
            isTrue(false, resultCode);
        }
        for (Object object : objects) {
            isNotNull(object, resultCode);
        }
    }

    /**
     * 结果是否不正确
     *
     * @param result     result
     * @param resultCode resultCode
     */
    public static void isNotTrue(boolean result, ResultCode resultCode) {
        isNotTrue(result, resultCode, null);
    }

    /**
     * 结果是否不正确
     *
     * @param result     result
     * @param resultCode resultCode
     * @param message    message
     */
    public static void isNotTrue(boolean result, ResultCode resultCode, String message) {
        isTrue(!result, resultCode, message);
    }

    /**
     * 更新成功个数大于0
     *
     * @param value      成功结果
     * @param resultCode resultCode
     */
    public static void updateSuccess(int value, ResultCode resultCode) {
        isTrue(MIN_COUNT < value, resultCode, null);
    }

    /**
     * 更新成功个数大于0
     *
     * @param value      成功结果
     * @param resultCode resultCode
     * @param message    message
     */
    public static void updateSuccess(int value, ResultCode resultCode, String message) {
        isTrue(MIN_COUNT < value, resultCode, message);
    }

    /**
     * 判断是否为true,如果不是则抛出业务异常，业务异常码为 errorCode
     *
     * @param result     result
     * @param resultCode resultCode
     */
    private static void isTrue(boolean result, ResultCode resultCode) {
        isTrue(result, resultCode, null);
    }

    /**
     * 判断是否为true,如果不是则抛出业务异常，业务异常码为 errorCode
     *
     * @param result     result
     * @param resultCode 错误码
     * @param message    错误信息
     */
    private static void isTrue(boolean result, ResultCode resultCode, String message) {
        isTrue(result, resultCode.getType(), resultCode.getCode(), null == message ? resultCode.getMessage() : message);
    }

    /**
     * 判断是否为true。如果不是则抛出业务异常，业务异常码为 errorCode
     *
     * @param result  result
     * @param type    错误类型
     * @param code    错误码
     * @param message 错误信息
     */
    private static void isTrue(boolean result, Integer type, String code, String message) {
        if (result) {
            return;
        }
        switch (type) {
            case ResultCode.PARAM:
                throw new ParamException(code, message);
            case ResultCode.BUSINESS:
                throw new BusinessException(code, message);
            case ResultCode.THIRD:
                throw new ThirdException(code, message);
            case ResultCode.SYSTEM:
                throw new SystemException(code, message);
            default:
                throw new RuntimeException(code + message);
        }
    }
}
