package com.general.extension.support;

import com.general.common.exception.SystemException;
import com.general.constants.SystemCode;
import com.general.nbhera.extension.ExtensionPoints;
import com.general.nbhera.extension.aonnotation.Strategy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @param <E> 泛型，表示具体的接口
 * @param <R> 决策对象泛型
 * @author xvanning
 * date: 2021/5/30 22:35
 * desc: 扩展点定义对象缓存
 */
@NoArgsConstructor
public class ExtensionCache<E extends ExtensionPoints<E, R>, R> {

    protected static final String NULL = "";

    /**
     * 扩展点实现类
     */
    private Class<E> targetClass;

    /**
     * 是否是策略模式扩展点
     */
    @Getter
    private Boolean isStrategy;

    /**
     * 扩展点code
     */
    private String code;

    /**
     * 无场景实现
     */
    private ExtensionDefinition<E, R> noScenarioExtension;

    /**
     * 场景code的map
     */
    private Map<String, ExtensionDefinition<E, R>> scenarioMap = Maps.newConcurrentMap();

    /**
     * 构造返回对象
     *
     * @param targetClass 扩展点class
     * @param code        模板code
     * @return 扩展点实现
     */
    public static ExtensionCache of(Class<?> targetClass, String code) {
        ExtensionCache cache = new ExtensionCache();
        cache.targetClass = targetClass;
        cache.code = code.trim();
        cache.isStrategy = targetClass.isAnnotationPresent(Strategy.class);
        return cache;
    }

    /**
     * 设置扩展点实现
     *
     * @param definition 扩展点定义
     * @return 设置对象
     */
    protected boolean put(ExtensionDefinition definition) {
        if (StringUtils.isBlank(definition.getScenario())) {
            if (null != this.noScenarioExtension && !this.noScenarioExtension.equals(definition)) {
                throw new SystemException(SystemCode.EXTENSION, "no scenario implements already exists class = " + this.targetClass + " ,code = " + this.code + ", find class = " + Lists.newArrayList(noScenarioExtension.getExtensionPoint(), definition.getExtensionPoint()));
            }
            this.noScenarioExtension = definition;
            return true;
        }

        // 如果场景code为空，则设置默认的场景code
        ExtensionDefinition oldDefinition = scenarioMap.put(definition.getScenario(), definition);
        if (null != oldDefinition && oldDefinition.equals(definition)) {
            throw new SystemException(SystemCode.EXTENSION, "scenario implements already exists class = " + this.targetClass + " ,code = " + this.code + ", find class = " + Lists.newArrayList(oldDefinition.getExtensionPoint(), definition.getExtensionPoint()));
        }

        return true;

    }

    /**
     * 根据场景code 获取扩展点code
     *
     * @param scenario 场景
     * @return 扩展点定义
     */
    protected ExtensionDefinition getExtensionPoint(String scenario) {
        if (StringUtils.isNotBlank(scenario)) {
            ExtensionDefinition definition = scenarioMap.get(scenario);
            if (null != definition) {
                return definition;
            }
        }
        return noScenarioExtension;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetClass, code);
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        ExtensionCache that = (ExtensionCache) obj;
        return Objects.equals(this.targetClass, that.targetClass) && Objects.equals(this.code, that.code);
    }
}
