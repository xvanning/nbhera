package com.general.extension.scenario.horizontal;

import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;

/**
 * @author xvanning
 * date: 2021/7/25 19:28
 * desc:  无人车运力
 */
@Extension(CategoryTemplate.CAR_TRANSPORT)
public class CarRealTransportNameExt implements RealTransportName{
    @Override
    public String getRealTransportName() {
        return "========== 无人车运力 ==========";
    }
}
