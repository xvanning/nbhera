package com.general.extension;

import com.general.extension.annotation.ExtensionSession;
import com.general.extension.biz.horizontal.paytype.PayTypeNameExt;
import com.general.extension.session.SecondSessionExt;
import com.general.nbhera.extension.aonnotation.BizParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xvanning
 * date: 2021/7/25 22:16
 * desc:
 */
@Component
public class DoubleSessionDemo {
    @Resource
    SecondSessionExt secondSessionExt;
    @Resource
    PayTypeNameExt payTypeNameExt;

    /**
     * 双层 @ExtensionSession
     * 垂直业务的场景例子
     *
     * @param businessType businessType
     * @return 业务类型描述
     */
    @ExtensionSession
    public String getDoubleSession(@BizParam("businessType") Integer businessType) {
        return secondSessionExt.getSecondSession();
    }

    /**
     * @param businessType 业务类型
     * @param payType      支付类型
     * @return 描述
     */
    @ExtensionSession
    public String getDoubleSessionWithHorizontalExt(@BizParam("businessType") Integer businessType, Integer payType) {
        return payTypeNameExt.reduce(payType).getPayTypeNameWithBusiness();
    }
}
