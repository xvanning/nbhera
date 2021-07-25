package com.general.extension.biz;

import com.general.nbhera.extension.aonnotation.BizParam;
import com.general.nbhera.extension.aonnotation.Scenario;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xvanning
 * date: 2021/7/25 19:43
 * desc: 测试请求类
 */
@Data
@AllArgsConstructor
public class RequestForTest {

    /**
     * 业务类型
     */
    @BizParam("businessType")
    private Integer businessType;

    /**
     * 运力类型
     */
    private Integer transType;

    /**
     * 场景
     */
    @Scenario
    private String scenario;
}
