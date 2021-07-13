package com.general.test;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/7/1 0:37
 * desc: session
 */
public class Session {

    /**
     * session 上下文
     */
    private Map<String, Object> context;

    /**
     * 是否测试模式
     */
    private Boolean test;

    /**
     * 构造方法
     *
     * @param test 是否测试模式
     */
    public Session(Boolean test) {
        if (null == test) {
            this.test = false;
        } else {
            this.test = test;
        }
    }

    /**
     * 是否是测试模式
     *
     * @return 是否是测试模式
     */
    public Boolean isTest() {
        return null != test && test;
    }

    /**
     * 设置上下文字段
     *
     * @param key   键名称
     * @param value 值
     * @return 设置上下文
     */
    public Object set(String key, Object value) {
        if (null == context) {
            context = Maps.newHashMap();
        }
        return context.put(key, value);
    }

    /**
     * 获取上下文内容
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return null == context ? null : context.get(key);
    }

}
