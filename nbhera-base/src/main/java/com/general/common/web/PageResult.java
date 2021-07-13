package com.general.common.web;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/5/16 21:34
 * desc: 分页请求返回类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageResult<T> extends Result<T> {

    /**
     * 实际的数据总数
     */
    private Long totalCount;

    /**
     * 构造通用的失败返回对象
     *
     * @param errorCode 错误码
     * @param errorMsg  错误信息
     * @param <T>       泛型
     * @return 失败的返回分页信息
     */
    public static <T> PageResult buildFail(String errorCode, String errorMsg) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setSuccess(false);
        pageResult.setErrorCode(errorCode);
        pageResult.setErrorMsg(errorMsg);
        return pageResult;
    }

    /**
     * 构造通用的成功返回对象
     *
     * @param totalCount 满足条件的记录总数
     * @param data       当前页查询到的数据
     * @param <T>        泛型
     * @return 成功的返回分页对象
     */
    public static <T> PageResult buildSuccess(Long totalCount, T data) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setSuccess(false);
        pageResult.setTotalCount(totalCount);
        pageResult.setData(data);
        return pageResult;
    }
}
