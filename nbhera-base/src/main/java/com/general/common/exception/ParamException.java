package com.general.common.exception;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/5/18 0:31
 * desc: 参数异常
 */
public class ParamException extends WithCodeException {
    public ParamException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ParamException(String code, String message, Map<String, Object> extension) {
        super(code, message, extension);
    }

    public ParamException(ResultCode code, String message, Map<String, Object> extension) {
        super(code, message, extension);
    }

    public ParamException(String code, String message) {
        super(code, message);
    }

    public ParamException(ResultCode code) {
        super(code);
    }

    public ParamException(ResultCode code, String message) {
        super(code, message);
    }

    public ParamException(ResultCode code, Map<String, Object> extension) {
        super(code, extension);
    }
}
