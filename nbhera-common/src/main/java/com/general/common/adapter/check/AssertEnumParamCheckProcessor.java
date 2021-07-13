package com.general.common.adapter.check;

import com.general.common.adapter.ParamCheckProcessor;
import com.general.common.annotation.AssertEnum;
import com.general.common.util.EnumUtils;

/**
 * @author xvanning
 * date: 2021/6/21 23:34
 * desc: 是否为指定枚举类型的参数校验处理器
 */
public class AssertEnumParamCheckProcessor implements ParamCheckProcessor<AssertEnum, Object> {
    @Override
    public boolean check(AssertEnum annotation, Class<?> valueClass, Object value) {
        return EnumUtils.isEnum(annotation.value(), value);
    }

    @Override
    public String getMessage(AssertEnum annotation, Object value) {
        return annotation.message();
    }
}
