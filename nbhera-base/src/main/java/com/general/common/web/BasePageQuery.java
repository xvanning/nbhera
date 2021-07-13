package com.general.common.web;

import lombok.Data;
import lombok.ToString;

/**
 * @author xvanning
 * date: 2021/5/16 21:23
 * desc: 分页请求类
 */
@Data
@ToString
public class BasePageQuery {

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 当前页
     */
    private Integer currentPage = 1;

    /**
     * 开始查询的记录数
     */
    private Integer startRow = 0;
}
