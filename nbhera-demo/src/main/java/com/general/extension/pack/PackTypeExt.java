package com.general.extension.pack;

import com.general.nbhera.extension.ExtensionPoints;
import com.general.nbhera.extension.aonnotation.Strategy;

/**
 * @author xvanning
 * date: 2021/7/15 0:06
 * desc:
 */
@Strategy
public interface PackTypeExt extends ExtensionPoints<PackTypeExt, Integer> {

    String getPackType();
}
