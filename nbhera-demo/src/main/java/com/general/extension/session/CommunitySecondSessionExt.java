package com.general.extension.session;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;

/**
 * @author xvanning
 * date: 2021/7/25 22:13
 * desc:
 */
@Extension(TemplateDemo.COMMUNITY_BIZ)
public class CommunitySecondSessionExt implements SecondSessionExt{
    @Override
    public String getSecondSession() {
        return "【第二层session】 社区业务";
    }
}
