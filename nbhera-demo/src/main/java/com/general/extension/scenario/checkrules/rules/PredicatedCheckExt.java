package com.general.extension.scenario.checkrules.rules;

import com.general.extension.annotation.Extension;

/**
 * @author xvanning
 * date: 2021/7/24 23:18
 * desc: 社区预测到站的校验扩展点
 */
@Extension("PREDICATED_CHECK")
public class PredicatedCheckExt implements CheckProcessExt{
    @Override
    public Boolean check() {
        return true;
    }
}
