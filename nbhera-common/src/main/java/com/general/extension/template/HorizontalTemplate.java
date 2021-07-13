package com.general.extension.template;

import com.general.common.util.AssertUtils;
import com.general.constants.SystemCode;
import com.general.extension.annotation.TemplateConfig;
import com.general.extension.enums.TemplateType;

import java.util.Collections;

/**
 * @author xvanning
 * date: 2021/6/6 23:19
 * desc: 基础水平模板
 */
public abstract class HorizontalTemplate extends BaseTemplate {

    /**
     * 构造函数
     */
    public HorizontalTemplate() {
        TemplateConfig templateConfig = this.getClass().getAnnotation(TemplateConfig.class);
        AssertUtils.isNotNull(templateConfig, SystemCode.EXTENSION, this.getClass() + "can not find @TemplateConfig");
        init(templateConfig.value(), TemplateType.HORIZONTAL, templateConfig.priority(), Collections.emptyList());
    }

    /**
     * 构造函数
     *
     * @param templateCode 模板code
     * @param priority     优先级
     */
    public HorizontalTemplate(String templateCode, Integer priority) {
        init(templateCode, TemplateType.HORIZONTAL, priority, Collections.emptyList());
    }
}
