package com.general.extension.scenario.checkrules;

import com.general.extension.annotation.Extension;
import com.general.extension.template.TemplateDemo;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author xvanning
 * date: 2021/7/24 23:03
 * desc: 校园业务校验规则
 */
@Extension(TemplateDemo.SCHOOL_BIZ)
public class SchoolCheckRules implements CheckRulesExt{
    @Override
    public List<String> getCheckRulesList() {
        return Lists.newArrayList("COMMON_CHECK","SCHOOL_CHECK");
    }
}
