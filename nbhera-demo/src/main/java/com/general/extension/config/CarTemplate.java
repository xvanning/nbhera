package com.general.extension.config;

import com.general.extension.annotation.TemplateConfig;
import com.general.extension.template.CategoryTemplate;
import com.general.extension.template.HorizontalTemplate;
import com.general.extension.template.TemplateDemo;

import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/7/17 23:36
 * desc: 无人车运力水平模板的统一 support 校验
 * 水平模板的 单独class实现的模板类
 */
@TemplateConfig(value = CategoryTemplate.CAR_TRANSPORT)
public class CarTemplate extends HorizontalTemplate {
    @Override
    public boolean checkSupport() {
        return true;
    }

    @Override
    public boolean support(Object reduceObject, String bizCode) {
        return Objects.equals(2, reduceObject);
    }
}
