package com.general.aop;

import lombok.Builder;
import lombok.Data;

/**
 * @author xvanning
 * date: 2021/8/25 22:44
 * desc:
 */
@Data
@Builder
public class ClientTestResp {

    private String result;

    private String sequence;
}
