package com.general.extension.strategy;

import com.general.extension.annotation.Extension;

/**
 * @author xvanning
 * date: 2021/7/15 0:07
 * desc:
 */
@Extension("big")
public class BigPackTypeExt implements PackTypeExt {
    @Override
    public String getPackType() {
        return "大大大大大";
    }

    @Override
    public boolean support(Integer type) {
        return type == 1;
    }
}
