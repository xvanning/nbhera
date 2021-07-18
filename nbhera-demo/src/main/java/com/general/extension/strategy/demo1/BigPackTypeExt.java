package com.general.extension.strategy.demo1;

import com.general.extension.annotation.Extension;

/**
 * @author xvanning
 * date: 2021/7/15 0:07
 * desc: 这边的 @Extension 不与任何业务模板绑定
 */
@Extension("big")
public class BigPackTypeExt implements PackTypeExt {
    @Override
    public String getPackType() {
        return "策略模式-大大大大包裹";
    }

    @Override
    public boolean support(Integer type) {
        return type == 1;
    }
}
