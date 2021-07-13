package com.general.common.adapter.check;

import com.general.common.adapter.ParamCheckProcessor;
import com.general.common.annotation.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author xvanning
 * date: 2021/6/22 0:09
 * desc: 不为空的参数校验执行器
 */
public class NotNullParamCheckProcessor implements ParamCheckProcessor<NotNull, Object> {
    @Override
    public boolean check(NotNull annotation, Class<?> valueClass, Object value) {
        if (null == value) {
            return false;
        }
        if (Collection.class.isAssignableFrom(valueClass)) {
            return CollectionUtils.isNotEmpty((Collection) value);
        }
        if (valueClass.isArray()) {
            return ((Object[]) value).length > 0;
        }
        if (Map.class.isAssignableFrom(valueClass)) {
            return MapUtils.isNotEmpty((Map) value);
        }
        return true;
    }

    @Override
    public String getMessage(NotNull annotation, Object o) {
        return StringUtils.isBlank(annotation.value()) ? "参数不能为空" : annotation.value();
    }
}
