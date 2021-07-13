package com.general.extension.template;

import com.general.extension.enums.TemplateType;
import com.general.extension.support.ExtensionDefinition;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * @author xvanning
 * date: 2021/5/30 21:45
 * desc: 基础模板类，用来增加横向业务约束
 */
@Getter
public abstract class BaseTemplate implements Template {

    /**
     * 默认优先级
     */
    protected static final int DEFAULT = -1;

    /**
     * 模板code
     */
    private String templateCode;

    /**
     * 模板类型
     */
    private TemplateType templateType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 订阅的模板
     */
    private List<String> subscribe;

    /**
     * 模板实现的扩展点
     */
    private List<Class<?>> extensionPointClasses = Lists.newArrayList();

    /**
     * 模板创建方法
     *
     * @param templateCode 模板code
     * @param templateType 模板类型
     * @param priority     优先级
     * @param subscribe    订阅的模板
     */
    public void init(String templateCode, TemplateType templateType, Integer priority, List<String> subscribe) {
        this.templateCode = templateCode;
        this.templateType = templateType;
        this.priority = priority;
        this.subscribe = subscribe;
    }

    @Override
    public List<Class<?>> getExtensionPointClasses() {
        return extensionPointClasses;
    }

    @Override
    public Boolean isEnable(Object reduceObject, String bizCode) {
        // 如果不需要校验则直接返回支持
        if (!checkSupport()) {
            return true;
        }
        return support(reduceObject, bizCode);
    }

    /**
     * 添加扩展点实现
     *
     * @param extensionDefinition 扩展点实现
     */
    public void addExtensionPoint(ExtensionDefinition extensionDefinition) {
        extensionPointClasses.add(extensionDefinition.getTargetClass());
    }

    /**
     * 移除扩展点实现
     *
     * @param extensionDefinition 扩展点
     */
    public void remove(ExtensionDefinition extensionDefinition) {
        extensionPointClasses.remove(extensionDefinition);
    }

    /**
     * 返回 当前模板是否需要检查 是否支持
     *
     * @return 当前模板是否需要检查 是否支持
     */
    public abstract boolean checkSupport();

    /**
     * 模板决策方法，决策当前模板是否是支持
     *
     * @param reduceObject 决策对象
     * @param bizCode      业务code
     * @return 是否支持
     */
    public abstract boolean support(Object reduceObject, String bizCode);
}
