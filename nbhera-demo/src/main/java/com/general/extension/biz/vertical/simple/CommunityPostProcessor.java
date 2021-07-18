package com.general.extension.biz.vertical.simple;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;

/**
 * @author xvanning
 * date: 2021/7/17 16:55
 * desc: 社区垂直业务
 */
@Extension(TemplateDemo.COMMUNITY_BIZ)
public class CommunityPostProcessor implements PostProcessorExt{
    @Override
    public String getCurrentBiz() {
        return "这是社区业务";
    }
}
