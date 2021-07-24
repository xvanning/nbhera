package com.general.extension.scenario.checkrules.rules;

import com.general.extension.annotation.Extension;

/**
 * @author xvanning
 * date: 2021/7/24 23:17
 * desc: 社区校验扩展点
 */
@Extension("COMMUNITY_CHECK")
public class CommunityCheckExt implements CheckProcessExt {
    @Override
    public Boolean check() {
        return true;
    }
}
