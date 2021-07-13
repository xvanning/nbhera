package com.general.common.adapter.result;

import com.general.common.adapter.ExceptionResultProcessor;
import com.general.common.web.PageResult;

/**
 * @author xvanning
 * date: 2021/6/21 23:36
 * desc: 分页结果的异常处理器
 */
public class PageResultExceptionResultProcessor implements ExceptionResultProcessor<PageResult<?>> {
    @Override
    public PageResult<?> convert(String errorCode, String message, Throwable throwable) {
        return PageResult.buildFail(errorCode, message);
    }
}
