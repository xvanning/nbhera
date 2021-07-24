package com.general.extension.template;

/**
 * @author xvanning
 * date: 2021/6/6 23:18
 * desc: 基础水平模板
 */
public class DefaultHorizontalTemplate extends HorizontalTemplate {

    /**
     * 重写构造方法
     *
     * @param code     code
     * @param priority 优先级
     */
    public DefaultHorizontalTemplate(String code, Integer priority) {
        super(code, priority);
    }

    @Override
    public boolean checkSupport() {
        return false;
    }

    @Override
    public boolean support(Object reduceObject, String bizCode) {
        return false;
    }
}
