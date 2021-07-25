package com.general.extension.session;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;

/**
 * @author xvanning
 * date: 2021/7/25 22:05
 * desc: 【第一层session】 社区业务
 */
@Extension(TemplateDemo.COMMUNITY_BIZ)
public class CommunityFirstSessionExt implements FirstSessionExt{
    @Override
    public String getFirstSession() {
        return "【第一层session】 社区业务";
    }
}
