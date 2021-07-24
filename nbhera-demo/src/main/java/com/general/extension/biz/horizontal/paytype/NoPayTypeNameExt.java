package com.general.extension.biz.horizontal.paytype;

import com.general.enums.PayTypeEnums;
import com.general.extension.annotation.Extension;
import com.general.extension.biz.vertical.simple.PostProcessorExt;
import com.general.extension.template.CategoryTemplate;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/7/17 23:33
 * desc: 无需支付水平模板
 */
@Extension(CategoryTemplate.NO_PAY)
public class NoPayTypeNameExt implements PayTypeNameExt {
    @Resource
    PostProcessorExt postProcessorExt;

    @Override
    public String getPayTypeName() {
        return "无需支付水平模板";
    }

    @Override
    public String getPayTypeNameWithBusiness() {
        System.out.println("======= 无需支付 =======");
        return postProcessorExt.getCurrentBiz();
    }

    @Override
    public boolean support(Integer reduceObject) {
        return Objects.equals(PayTypeEnums.NO_PAY.getCode(), reduceObject);
    }
}
