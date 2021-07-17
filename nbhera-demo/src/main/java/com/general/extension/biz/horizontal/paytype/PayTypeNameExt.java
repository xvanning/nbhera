package com.general.extension.biz.horizontal.paytype;

import com.general.nbhera.extension.ExtensionPoints;

/**
 * @author xvanning
 * date: 2021/7/17 23:19
 * desc: 支付类型水平模板
 */
public interface PayTypeNameExt extends ExtensionPoints<PayTypeNameExt, Integer> {

    String getPayTypeName();
}
