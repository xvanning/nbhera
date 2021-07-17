package com.general.extension.strategy;

import com.general.nbhera.extension.ExtensionPoints;
import com.general.nbhera.extension.aonnotation.Strategy;

/**
 * @author xvanning
 * date: 2021/7/15 0:06
 * desc: 策略模式demo，演示不同包裹类型进行不同的处理
 */
@Strategy
public interface PackTypeExt extends ExtensionPoints<PackTypeExt, Integer> {

    String getPackType();
}
