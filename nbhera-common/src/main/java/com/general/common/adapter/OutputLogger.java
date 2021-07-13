package com.general.common.adapter;

import com.general.common.exception.WithCodeException;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/6/21 23:57
 * desc: 对外拦截器的日志输出，可以针对接口aop重写日志输出
 */
public interface OutputLogger {

    /**
     * 记录接口请求参数
     *
     * @param methodName 方法签名（当前类名 + "." + 方法名）
     * @param args       接口入参
     */
    void logRequest(String methodName, Object[] args);

    /**
     * 记录非param、business的异常
     *
     * @param methodName        方法签名（当前类名 + "." + 方法名）
     * @param extension         异常扩展信息，withCodeException.getExtension
     * @param withCodeException withCodeException
     */
    void logSystemError(String methodName, Map<String, Object> extension, WithCodeException withCodeException);

    /**
     * 记录接口发生的异常，这里主要为非 WithCodeException 的未知参数
     *
     * @param methodName 方法签名（当前类名 + "." + 方法名）
     * @param throwable  异常信息
     */
    void logThrowable(String methodName, Throwable throwable);

    /**
     * 记录接口返回值
     *
     * @param methodName     方法签名（当前类名 + "." + 方法名）
     * @param response       接口返回值
     * @param success        是否正常处理
     * @param throwException 是否会抛出异常，如果为 true 则response为null
     * @param costTime       接口处理时间
     */
    void logResponse(String methodName, Object response, boolean success, boolean throwException, Long costTime);
}
