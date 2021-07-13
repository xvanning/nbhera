package com.general.extension.template;

import com.general.extension.enums.TemplateType;

import java.util.List;

/**
 * @author xvanning
 * date: 2021/5/30 21:46
 * desc: 模板接口定义
 */
public interface Template {

    /**
     * 获取 模板code
     *
     * @return 模板code
     */
    String getTemplateCode();

    /**
     * 返回模板类型
     *
     * @return 模板类型
     */
    TemplateType getTemplateType();

    /**
     * 获取 订阅的模板
     *
     * @return 订阅的模板
     */
    List<String> getSubscribe();

    /**
     * 模板优先级
     *
     * @return 模板优先级
     */
    Integer getPriority();

    /**
     * 根据决策对象校验模板是否支持
     *
     * @param reduceObject 决策对象
     * @param bizCode      业务code
     * @return 是否支持
     */
    Boolean isEnable(Object reduceObject, String bizCode);

    /**
     * 返回模板实现的扩展点
     *
     * @return 扩展点列表
     */
    List<Class<?>> getExtensionPointClasses();
}
