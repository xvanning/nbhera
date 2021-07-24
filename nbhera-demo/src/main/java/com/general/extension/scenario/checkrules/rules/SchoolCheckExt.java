package com.general.extension.scenario.checkrules.rules;

import com.general.extension.annotation.Extension;

/**
 * @author xvanning
 * date: 2021/7/24 23:17
 * desc: 校园的校验扩展点
 */
@Extension("SCHOOL_CHECK")
public class SchoolCheckExt implements CheckProcessExt{
    @Override
    public Boolean check() {
        return true;
    }
}
