package com.general.extension.biz.horizontal.transport;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;

/**
 * @author xvanning
 * date: 2021/7/17 23:24
 * desc: 小件员运力水平模板
 */
@Extension(TemplateDemo.COURIER_TRANSPORT)
public class CourierTransportExt implements TransportNameExt {
    @Override
    public String getTransportName() {
        return "小件员运力水平模板";
    }
}
