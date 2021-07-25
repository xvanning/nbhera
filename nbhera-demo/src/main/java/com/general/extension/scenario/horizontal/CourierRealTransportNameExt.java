package com.general.extension.scenario.horizontal;

import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;

/**
 * @author xvanning
 * date: 2021/7/25 19:23
 * desc:
 */
@Extension(CategoryTemplate.COURIER_TRANSPORT)
public class CourierRealTransportNameExt implements RealTransportName {
    @Override
    public String getRealTransportName() {
        return "========== 人力运力 ==========";
    }
}
