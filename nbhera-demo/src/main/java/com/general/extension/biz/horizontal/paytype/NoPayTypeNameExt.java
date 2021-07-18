package com.general.extension.biz.horizontal.paytype;

import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;

import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/7/17 23:33
 * desc: 无需支付水平模板
 */
@Extension(CategoryTemplate.NO_PAY)
public class NoPayTypeNameExt implements PayTypeNameExt {
    @Override
    public String getPayTypeName() {
        return "无需支付水平模板";
    }

    @Override
    public boolean support(Integer reduceObject) {
        return Objects.equals(2, reduceObject);
    }
}
