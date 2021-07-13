package com.general.common.web;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <T> 结果泛型
 * @author xvanning
 * date: 2021/5/16 21:36
 * desc: web返回包装类
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 调用链ID
     */
    private String traceId;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 扩展信息
     */
    private Map<String, Object> otherData;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 构建成功的返回结果
     *
     * @param data data
     * @param <E>  泛型
     * @return 返回结果
     */
    public static <E> Result<E> success(E data) {
        Result<E> resp = new Result<>();
        resp.setSuccess(true);
        resp.setData(data);
        return resp;
    }

    /**
     * 构建成功的返回结果
     *
     * @param errorCode 错误码
     * @param errorMsg  错误信息
     * @param <E>       泛型
     * @return 返回结果
     */
    public static <E> Result<E> failed(String errorCode, String errorMsg) {
        Result<E> resp = new Result<>();
        resp.setSuccess(true);
        resp.setErrorCode(errorCode);
        resp.setErrorMsg(errorMsg);
        return resp;
    }

    /**
     * 是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * 获取扩展信息
     *
     * @param k 键
     * @return value
     */
    public Object getOtherData(String k) {
        return null == this.otherData ? null : this.otherData.get(k);
    }

    /**
     * 添加扩展信息
     *
     * @param k key
     * @param v value
     */
    public void addOtherData(String k, Object v) {
        if (null == this.otherData) {
            this.otherData = new HashMap<>(16);
        }
        this.otherData.put(k, v);
    }

}
