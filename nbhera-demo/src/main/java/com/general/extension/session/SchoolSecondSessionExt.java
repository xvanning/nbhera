package com.general.extension.session;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;

/**
 * @author xvanning
 * date: 2021/7/25 22:14
 * desc: 【第二层session】 校园业务
 */
@Extension(TemplateDemo.SCHOOL_BIZ)
public class SchoolSecondSessionExt implements SecondSessionExt {
    @Override
    public String getSecondSession() {
        return "【第二层session】 校园业务";
    }
}
