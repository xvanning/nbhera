package com.general.extension.scenario.horizontal;

import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;
import com.general.extension.template.ScenarioConstant;

/**
 * @author xvanning
 * date: 2021/7/25 19:29
 * desc: 无人车运力 -- 卡车运送
 */
@Extension(value = CategoryTemplate.CAR_TRANSPORT, scenario = ScenarioConstant.TRUCK)
public class CarTruckRealTransportNameExt implements RealTransportName {
    @Override
    public String getRealTransportName() {
        return "========== 无人车运力 -- 卡车运送 ==========";
    }
}
