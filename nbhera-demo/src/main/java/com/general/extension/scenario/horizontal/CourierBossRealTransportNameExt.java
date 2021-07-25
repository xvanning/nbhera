package com.general.extension.scenario.horizontal;

import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;
import com.general.extension.template.ScenarioConstant;

/**
 * @author xvanning
 * date: 2021/7/25 19:25
 * desc: 人力运力 -- 老板运送
 */
@Extension(value = CategoryTemplate.COURIER_TRANSPORT, scenario = ScenarioConstant.BOSS)
public class CourierBossRealTransportNameExt implements RealTransportName {
    @Override
    public String getRealTransportName() {
        return "========== 人力运力 -- 老板运送 ==========";
    }
}
