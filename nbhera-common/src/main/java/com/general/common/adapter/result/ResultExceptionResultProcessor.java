package com.general.common.adapter.result;

import com.general.common.adapter.ExceptionResultProcessor;
import com.general.common.web.Result;

/**
 * @author xvanning
 * date: 2021/6/21 23:36
 * desc: RPC异常结果处理器
 */
public class ResultExceptionResultProcessor implements ExceptionResultProcessor<Result<?>> {
    @Override
    public Result<?> convert(String errorCode, String message, Throwable throwable) {
        return Result.failed(errorCode, message);
    }
}
