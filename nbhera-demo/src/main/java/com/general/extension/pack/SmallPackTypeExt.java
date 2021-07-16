package com.general.extension.pack;

import com.general.extension.annotation.Extension;

/**
 * @author xvanning
 * date: 2021/7/15 0:11
 * desc:
 */
@Extension("small")
public class SmallPackTypeExt implements PackTypeExt{
    @Override
    public String getPackType() {
        return "小小小小小";
    }

    @Override
    public boolean support(Integer type) {
        return type == 2;
    }
}
