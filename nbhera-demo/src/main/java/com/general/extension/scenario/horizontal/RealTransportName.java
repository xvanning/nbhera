package com.general.extension.scenario.horizontal;

import com.general.nbhera.extension.ExtensionPoints;
import com.general.nbhera.extension.aonnotation.Strategy;

/**
 * @author xvanning
 * date: 2021/7/25 19:22
 * desc: 获取真实的运力名称
 */
public interface RealTransportName extends ExtensionPoints<RealTransportName, Integer> {

    String getRealTransportName();
}
