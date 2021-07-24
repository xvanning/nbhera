package com.general.extension.scenario.checkrules;

import com.general.extension.annotation.Extension;
import com.general.extension.template.ScenarioConstant;
import com.general.extension.template.TemplateDemo;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author xvanning
 * date: 2021/7/24 23:07
 * desc: 社区预测到站的场景
 */
@Extension(value = TemplateDemo.COMMUNITY_BIZ,scenario = ScenarioConstant.PREDICATED)
public class PredicateCommunityRules implements CheckRulesExt{

    @Override
    public List<String> getCheckRulesList() {
        return Lists.newArrayList("COMMON_CHECK","PREDICATED_CHECK");
    }
}
