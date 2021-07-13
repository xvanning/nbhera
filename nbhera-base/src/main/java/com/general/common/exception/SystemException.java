package com.general.common.exception;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/5/18 0:32
 * desc: 系统异常
 */
public class SystemException extends WithCodeException {
    public SystemException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public SystemException(String code, String message, Map<String, Object> extension) {
        super(code, message, extension);
    }

    public SystemException(ResultCode code, String message, Map<String, Object> extension) {
        super(code, message, extension);
    }

    public SystemException(String code, String message) {
        super(code, message);
    }

    public SystemException(ResultCode code) {
        super(code);
    }

    public SystemException(ResultCode code, String message) {
        super(code, message);
    }

    public SystemException(ResultCode code, Map<String, Object> extension) {
        super(code, extension);
    }
}
