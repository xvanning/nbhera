package com.general.extension.config;

import com.general.extension.annotation.TemplateConfig;
import com.general.extension.template.CategoryTemplate;
import com.general.extension.template.HorizontalTemplate;
import com.general.extension.template.TemplateDemo;

import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/7/17 23:38
 * desc: 小件员运力水平模板的统一 support 校验
 */
@TemplateConfig(value = CategoryTemplate.COURIER_TRANSPORT)
public class CourierTemplate extends HorizontalTemplate {
    @Override
    public boolean checkSupport() {
        return false;
    }

    @Override
    public boolean support(Object reduceObject, String bizCode) {
        return Objects.equals(1, reduceObject);
    }
}
