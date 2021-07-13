package com.general.extension.support;

import com.general.extension.enums.TemplateType;
import com.general.extension.template.Template;
import com.general.nbhera.extension.ExtensionPoints;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @param <E> 扩展点泛型
 * @param <R> 扩展点决策对象泛型
 * @author xvanning
 * date: 2021/5/30 22:09
 * desc: 扩展点定义对象
 */
@Getter
@NoArgsConstructor
public class ExtensionDefinition<E extends ExtensionPoints<E, R>, R> implements Comparable<ExtensionDefinition> {

    /**
     * 扩展点code
     */
    private String code;

    /**
     * 模板类型，水平业务或者垂直业务
     */
    private Template template;

    /**
     * 场景code
     */
    private String scenario;

    /**
     * 是否默认实现
     */
    private Boolean isDefault;

    /**
     * 实现的扩展点类
     */
    private Class<E> targetClass;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 扩展点实现类
     */
    private E extensionPoint;

    /**
     * 扩展点定义
     *
     * @param code           扩展点code
     * @param scenario       场景
     * @param targetClass    扩展点类
     * @param extensionPoint 扩展点的实现
     * @param priority       扩展点优先级
     * @param isDefault      是否是默认扩展点
     * @return 扩展点定义
     */
    public static ExtensionDefinition of(String code, String scenario, Class<? extends ExtensionPoints> targetClass, Object extensionPoint, Integer priority, Boolean isDefault) {
        ExtensionDefinition definition = new ExtensionDefinition();
        definition.code = isDefault ? "DEFAULT" : code.trim();
        definition.scenario = StringUtils.isBlank(scenario) ? ExtensionCache.NULL : scenario;
        definition.targetClass = targetClass;
        definition.extensionPoint = (ExtensionPoints) extensionPoint;
        definition.priority = priority;
        definition.isDefault = isDefault;
        return definition;
    }

    /**
     * 设置模板
     *
     * @param template 模板
     */
    protected void setTemplate(Template template) {
        if (template != null) {
            this.template = template;
            if (this.priority == 2000) {
                this.priority = template.getPriority();
            }
        }
    }

    /**
     * 是否支持当前扩展点
     *
     * @param reduceTarget 决策对象
     * @param bizCode      业务code
     * @return 是否支持
     */
    public Boolean support(R reduceTarget, String bizCode) {
        return extensionPoint.support(reduceTarget) && (getType() == TemplateType.FREEDOM || template.isEnable(reduceTarget, bizCode));
    }

    /**
     * 获取模板类型
     *
     * @return 模板类型
     */
    public TemplateType getType() {
        return null == template ? TemplateType.FREEDOM : template.getTemplateType();
    }

    /**
     * 返回扩展点实际实现类
     *
     * @return 实现类
     */
    public Class<?> getExtensionPointsClass() {
        return this.extensionPoint.getClass();
    }


    @Override
    public int compareTo(ExtensionDefinition that) {
        // 有限按照优先级排序
        int result = this.priority.compareTo(that.priority);
        if (result != 0) {
            return result;
        }
        // 最后按照code进行排序
        return this.code.compareTo(that.code);
    }


    @Override
    public int hashCode() {
        return Objects.hash(code, scenario, extensionPoint);
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || this.getClass() != obj.getClass()) {
            return false;
        }
        ExtensionDefinition that = (ExtensionDefinition) obj;
        return Objects.equals(this.code, that.code) && Objects.equals(this.scenario, that.scenario) && Objects.equals(this.extensionPoint, that.extensionPoint);
    }

    @Override
    public String toString() {
        return "code='" + code + "\'" + ", type=" + getType() + (StringUtils.isBlank(scenario) ? "" : ", scenario=" + scenario + "\'") + (isDefault ? ", DEFAULT" : "") + ", targetClass=" + targetClass + ", priority=" + priority + ", extensionPoint=" + this.getExtensionPointsClass();
    }
}
