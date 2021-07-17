package com.general.extension.biz.horizontal.paytype;

import com.general.extension.annotation.Extension;
import com.general.extension.template.CategoryTemplate;
import com.general.extension.template.TemplateDemo;

import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/7/17 23:35
 * desc: 需要支付水平模板
 */
@Extension(CategoryTemplate.NEED_PAY)
public class NeedPayTypeNameExt implements PayTypeNameExt {
    @Override
    public String getPayTypeName() {
        return "需要支付水平模板";
    }

    @Override
    public boolean support(Integer reduceObject) {
        return Objects.equals(1, reduceObject);
    }
}
