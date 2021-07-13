package com.general.extension.support;

import com.general.nbhera.extension.ExtensionPoints;

import java.util.List;

/**
 * @param <E> 扩展点泛型
 * @param <R> 扩展点决策对象泛型
 * @author xvanning
 * date: 2021/6/6 21:42
 * desc: 策略模式执行器
 */
public final class StrategyExtensionExecutor<E extends ExtensionPoints<E, R>, R> extends ExtensionExecutor<E, R> {


    /**
     * 构造扩展点执行器
     *
     * @param scenario       场景code
     * @param reduceObject   决策对象
     * @param extensionClass 扩展点类
     * @param <E>            扩展点泛型
     * @param <R>            扩展点决策对象泛型
     * @return 扩展点执行器
     */
    public static <E extends ExtensionPoints<E, R>, R> StrategyExtensionExecutor of(String scenario, R reduceObject, Class<E> extensionClass) {
        List<ExtensionDefinition<E, R>> extensionPoints = ExtensionRegistryCenter.getStrategy(scenario, reduceObject, extensionClass);
        return new StrategyExtensionExecutor(scenario, reduceObject, extensionClass, extensionPoints);
    }

    /**
     * 构造方法
     *
     * @param scenario             场景
     * @param reduceObject         决策对象
     * @param extensionClass       扩展点接口类
     * @param extensionDefinitions 扩展点实现
     */
    private StrategyExtensionExecutor(String scenario, R reduceObject, Class<E> extensionClass, List<ExtensionDefinition<E, R>> extensionDefinitions) {
        super(scenario, reduceObject, extensionClass, extensionDefinitions);
    }

    @Override
    protected StringBuilder builder(StringBuilder stringBuilder) {
        return stringBuilder;
    }
}
