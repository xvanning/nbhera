package com.general.common.exception;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/5/18 0:32
 * desc:
 */
public class ThirdException extends WithCodeException {
    public ThirdException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ThirdException(String code, String message, Map<String, Object> extension) {
        super(code, message, extension);
    }

    public ThirdException(ResultCode code, String message, Map<String, Object> extension) {
        super(code, message, extension);
    }

    public ThirdException(String code, String message) {
        super(code, message);
    }

    public ThirdException(ResultCode code) {
        super(code);
    }

    public ThirdException(ResultCode code, String message) {
        super(code, message);
    }

    public ThirdException(ResultCode code, Map<String, Object> extension) {
        super(code, extension);
    }
}
