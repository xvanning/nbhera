package com.general.extension.scenario.checkrules;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author xvanning
 * date: 2021/7/24 23:02
 * desc: 社区业务校验规则
 */
@Extension(TemplateDemo.COMMUNITY_BIZ)
public class CommunityCheckRulesExt implements CheckRulesExt{
    @Override
    public List<String> getCheckRulesList() {
        return Lists.newArrayList("COMMON_CHECK","COMMUNITY_CHECK");
    }
}
