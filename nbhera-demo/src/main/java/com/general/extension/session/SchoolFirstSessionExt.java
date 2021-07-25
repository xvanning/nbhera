package com.general.extension.session;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;

/**
 * @author xvanning
 * date: 2021/7/25 22:09
 * desc: 【第一层session】 校园业务
 */
@Extension(TemplateDemo.SCHOOL_BIZ)
public class SchoolFirstSessionExt implements FirstSessionExt{
    @Override
    public String getFirstSession() {
        return "【第一层session】 校园业务";
    }
}
