package com.general.extension.scenario.checkrules.rules;

import com.general.extension.annotation.Extension;

/**
 * @author xvanning
 * date: 2021/7/24 23:15
 * desc: 通用校验
 */
@Extension("COMMON_CHECK")
public class CommonCheckExt implements CheckProcessExt{
    @Override
    public Boolean check() {
        return true;
    }
}
