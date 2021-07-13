package com.general.test;

import com.general.test.annotation.NbTest;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/7/1 0:37
 * desc: 测试模式民众规则适配器
 */
public interface ExpressionTestContextAdapter {

    /**
     * 默认命名空间
     */
    String DEFAULT = "DEFAULT";

    /**
     * 获取处理器命名空间，这样接口可以进行适配器指定，默认不指定的时候为 DEFAULT
     *
     * @return 命名空间
     */
    String getNameSpace();

    /**
     * 根据环境变量参数和表达式返回是否命中白名单规则，命中规则后不进行代理，执行原有方法
     *
     * @param env        根据入参组装成环境变量进行计算使用
     * @param expression 表达式
     * @return 是否命中
     */
    Boolean isMatch(Map<String, Object> env, String expression);

    /**
     * 命中后，返回值处理方法
     *
     * @param returnType 返回值类型
     * @param nbTest     注解配置信息
     * @param args       入参
     * @return 返回值
     */
    Object defaultResult(Class<?> returnType, NbTest nbTest, Object[] args);
}
