package com.general.extension.biz.horizontal.transport;

import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;
import com.general.extension.template.TemplateDemo;

/**
 * @author xvanning
 * date: 2021/7/17 23:22
 * desc: 无人车水平模板
 */
@Extension(CategoryTemplate.CAR_TRANSPORT)
public class CarTransportExt implements TransportNameExt {
    @Override
    public String getTransportName() {
        return "无人车运力水平模板";
    }
}
