package com.general.extension.scenario.checkrules;

import com.general.nbhera.extension.ExtensionPoints;

import java.util.List;

/**
 * @author xvanning
 * date: 2021/7/24 22:54
 * desc: 规则校验扩展点接口，用于测试 场景
 */
public interface CheckRulesExt extends ExtensionPoints<CheckRulesExt, Object> {

    /**
     * 获取校验规则的列表
     *
     * @return 校验规则的列表
     */
    List<String> getCheckRulesList();
}
