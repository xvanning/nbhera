package com.general.extension.biz.vertical;

import com.general.extension.annotation.ExtensionSession;
import com.general.nbhera.extension.aonnotation.BizParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xvanning
 * date: 2021/7/17 17:00
 * desc:
 */
@Component
public class VerticalBizDemo {

    @Resource
    PostProcessorExt postProcessorExt;

    @ExtensionSession
    public String getCurrentBiz(@BizParam("businessType") Integer businessType) {
        return postProcessorExt.getCurrentBiz();
    }
}
