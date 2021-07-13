package com.general.common.exception;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/5/18 0:29
 * desc: 业务异常
 */
public class BusinessException extends WithCodeException {

    public BusinessException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BusinessException(String code, String message, Map<String, Object> extension) {
        super(code, message, extension);
    }

    public BusinessException(ResultCode code, String message, Map<String, Object> extension) {
        super(code, message, extension);
    }

    public BusinessException(String code, String message) {
        super(code, message);
    }

    public BusinessException(ResultCode code) {
        super(code);
    }

    public BusinessException(ResultCode code, String message) {
        super(code, message);
    }

    public BusinessException(ResultCode code, Map<String, Object> extension) {
        super(code, extension);
    }
}
