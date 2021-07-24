package com.general.extension.scenario.checkrules.rules;

import com.general.nbhera.extension.ExtensionPoints;

/**
 * @author xvanning
 * date: 2021/7/24 23:15
 * desc: 执行校验的扩展点
 */
public interface CheckProcessExt extends ExtensionPoints<CheckProcessExt, Object> {

    Boolean check();
}
