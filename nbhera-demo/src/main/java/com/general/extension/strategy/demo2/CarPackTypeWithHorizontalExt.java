package com.general.extension.strategy.demo2;

import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;

/**
 * @author xvanning
 * date: 2021/7/15 0:07
 * desc: 这边的 @Extension 与 水平模板 绑定
 */
@Extension(CategoryTemplate.CAR_TRANSPORT)
public class CarPackTypeWithHorizontalExt implements PackTypeWithHorizontalExt {
    @Override
    public String getPackType() {
        return "策略模式-无人车包裹";
    }

    @Override
    public boolean support(Integer type) {
        return type == 1;
    }
}
