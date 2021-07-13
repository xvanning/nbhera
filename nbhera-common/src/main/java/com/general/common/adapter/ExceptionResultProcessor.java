package com.general.common.adapter;

/**
 * @param <Result> 结果类型的泛型
 * @author xvanning
 * date: 2021/6/21 23:31
 * desc: 异常结果处理器
 */
public interface ExceptionResultProcessor<Result> {


    /**
     * 如果结果发生异常，且当前接口返回类型与当前结果泛型一致，则会使用当前方法，对结果进行加工处理
     *
     * @param errorCode 异常码
     * @param message   错误信息
     * @param throwable 异常信息
     * @return 返回结果
     */
    Result convert(String errorCode, String message, Throwable throwable);
}
