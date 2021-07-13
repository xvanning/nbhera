package com.general.test.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.general.common.bean.GlobalConfig;
import com.general.test.ExpressionTestContextAdapter;
import com.general.test.annotation.NbTest;
import com.googlecode.aviator.AviatorEvaluator;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/7/1 0:50
 * desc: 表达式默认处理器
 */
public class DefaultExpressionTestContextAdapter implements ExpressionTestContextAdapter {
    @Override
    public String getNameSpace() {
        return DEFAULT;
    }

    @Override
    public Boolean isMatch(Map<String, Object> env, String expression) {
        // 如果没有配置表达式则不会命中规则
        if (StringUtils.isBlank(expression)) {
            return false;
        }
        return (Boolean) AviatorEvaluator.execute(expression, env, true);
    }

    @Override
    public Object defaultResult(Class<?> returnType, NbTest nbTest, Object[] args) {
        String defReturn = nbTest.defReturn();
        if (returnType == Void.class) {
            return null;
        }
        if (GlobalConfig.isNative(returnType)) {
            return TypeUtils.cast(defReturn, returnType, null);
        }
        return JSON.parseObject(nbTest.defReturn(), returnType);
    }
}
