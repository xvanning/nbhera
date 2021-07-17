package com.general.extension.biz.vertical;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;

/**
 * @author xvanning
 * date: 2021/7/17 16:54
 * desc: 校园垂直业务
 */
@Extension(TemplateDemo.SCHOOL_BIZ)
public class SchoolPostProcessorExt implements PostProcessorExt{
    @Override
    public String getCurrentBiz() {
        return "这是校园业务";
    }
}
