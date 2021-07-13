package com.general.common.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xvanning
 * date: 2021/5/18 0:08
 * desc: 基础带异常码的异常
 */
@Getter
public class WithCodeException extends RuntimeException {

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 扩展信息
     */
    private Map<String, Object> extension;

    /**
     * 带错误码、错误信息、异常的 构造函数
     *
     * @param code    错误码
     * @param message 错误码
     * @param cause   异常
     */
    public WithCodeException(String code, String message, Throwable cause) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 带错误码、错误信息、异常的 构造函数
     *
     * @param code      错误码
     * @param message   错误码
     * @param extension 扩展信息
     */
    public WithCodeException(String code, String message, Map<String, Object> extension) {
        super(message);
        this.code = code;
        this.message = message;
        this.extension = extension;
    }

    /**
     * result code 的构造函数
     *
     * @param code      code
     * @param message   message
     * @param extension extension
     */
    public WithCodeException(ResultCode code, String message, Map<String, Object> extension) {
        this(code.getCode(), null == message ? code.getMessage() : message, extension);
    }

    /**
     * 缺少扩展信息的构造函数
     *
     * @param code    code
     * @param message message
     */
    public WithCodeException(String code, String message) {
        this(code, message, new HashMap<>(16));
    }

    /**
     * 单独result code 的构造函数
     *
     * @param code code
     */
    public WithCodeException(ResultCode code) {
        this(code, null, null);
    }

    /**
     * result code 的构造函数，缺少扩展信息
     *
     * @param code    code
     * @param message message
     */
    public WithCodeException(ResultCode code, String message) {
        this(code, message, null);
    }

    /**
     * result code 的构造函数，缺少message
     *
     * @param code      code
     * @param extension extension
     */
    public WithCodeException(ResultCode code, Map<String, Object> extension) {
        this(code, null, extension);
    }


    /**
     * 添加扩展信息
     *
     * @param key   key
     * @param value value
     * @return 当前实例
     */
    public WithCodeException addExtension(String key, Object value) {
        if (null == extension) {
            extension = new HashMap<>(16);
        }
        extension.put(key, value);
        return this;
    }


}
