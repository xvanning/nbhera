package com.general.extension.session;

import com.general.nbhera.extension.ExtensionPoints;

/**
 * @author xvanning
 * date: 2021/7/25 22:02
 * desc: 第一个session扩展点
 */
public interface FirstSessionExt extends ExtensionPoints<FirstSessionExt, Object> {

    /**
     * 获取第一个session名称
     *
     * @return 第一个session名称
     */
    String getFirstSession();
}
