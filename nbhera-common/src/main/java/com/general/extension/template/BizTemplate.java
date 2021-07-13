package com.general.extension.template;

import com.general.common.util.AssertUtils;
import com.general.constants.SystemCode;
import com.general.extension.annotation.TemplateConfig;
import com.general.extension.enums.TemplateType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author xvanning
 * date: 2021/6/6 23:03
 * desc: 业务模板
 */
public class BizTemplate extends BaseTemplate {

    /**
     * 默认构造方法
     */
    public BizTemplate() {
        TemplateConfig templateConfig = this.getClass().getAnnotation(TemplateConfig.class);
        AssertUtils.isNotNull(templateConfig, SystemCode.EXTENSION, this.getClass() + "can not find @TemplateConfig");
        init(templateConfig.value(), templateConfig.priority(), templateConfig.subscribe());
    }

    /**
     * 业务模板
     *
     * @param templateCode 业务模板code
     * @param priority     优先级
     * @param subscribe    订阅的模板
     */
    public BizTemplate(String templateCode, Integer priority, String[] subscribe) {
        init(templateCode, priority, subscribe);
    }

    /**
     * 初始化模板
     *
     * @param templateCode 模板code
     * @param priority     优先级
     * @param subscribe    订阅的模板
     */
    private void init(String templateCode, int priority, String[] subscribe) {
        Set<String> templateCodeSet = Sets.newHashSet(templateCode);
        for (String subscribeCode : subscribe) {
            if (StringUtils.isNotBlank(subscribeCode)) {
                templateCodeSet.add(subscribeCode.trim());
            }
        }
        init(templateCode, TemplateType.VERTICAL, priority, Lists.newArrayList(templateCodeSet));
    }

    @Override
    public boolean checkSupport() {
        return true;
    }

    @Override
    public boolean support(Object reduceObject, String bizCode) {
        return Objects.equals(getTemplateCode(), bizCode);
    }
}
