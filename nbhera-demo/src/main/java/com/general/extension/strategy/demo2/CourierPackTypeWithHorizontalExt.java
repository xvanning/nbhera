package com.general.extension.strategy.demo2;

import com.general.enums.TransTypeEnums;
import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;

/**
 * @author xvanning
 * date: 2021/7/15 0:11
 * desc: 这边的 @Extension 不与 水平模板 绑定
 */
@Extension(CategoryTemplate.COURIER_TRANSPORT)
public class CourierPackTypeWithHorizontalExt implements PackTypeWithHorizontalExt {
    @Override
    public String getPackType() {
        return "策略模式-小件员包裹";
    }

    @Override
    public boolean support(Integer type) {
        return type == TransTypeEnums.COURIER.getCode();
    }
}
