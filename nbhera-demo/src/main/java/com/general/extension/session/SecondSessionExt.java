package com.general.extension.session;

import com.general.nbhera.extension.ExtensionPoints;

/**
 * @author xvanning
 * date: 2021/7/25 22:11
 * desc: 第二层session
 */
public interface SecondSessionExt extends ExtensionPoints<SecondSessionExt, Object> {
    /**
     * 获取第二层session名称
     *
     * @return 第二层session名称
     */
    String getSecondSession();
}
