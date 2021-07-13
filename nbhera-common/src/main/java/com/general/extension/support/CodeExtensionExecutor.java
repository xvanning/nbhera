package com.general.extension.support;

import com.general.nbhera.extension.ExtensionPoints;
import lombok.Getter;

import java.util.List;

/**
 * @param <E> 扩展点泛型
 * @param <R> 扩展点决策对象泛型
 * @author xvanning
 * date: 2021/6/6 21:27
 * desc: 指定code执行器
 */
@Getter
public class CodeExtensionExecutor<E extends ExtensionPoints<E, R>, R> extends ExtensionExecutor<E, R> {

    /**
     * 扩展点code
     */
    private List<String> extensionCodes;

    /**
     * 构造扩展点执行器
     *
     * @param extensionCodes 扩展点code
     * @param scenario       场景code
     * @param reduceObject   决策对象
     * @param extensionClass 扩展点接口类
     * @param <E>            扩展点泛型
     * @param <R>            扩展点决策对象泛型
     * @return 扩展执行器
     */
    public static <E extends ExtensionPoints<E, R>, R> CodeExtensionExecutor of(List<String> extensionCodes, String scenario, R reduceObject, Class<E> extensionClass) {
        List<ExtensionDefinition<E, R>> extensionDefinitions = ExtensionRegistryCenter.getExtensionDefinitionList(extensionCodes, scenario, reduceObject, extensionClass);
        return new CodeExtensionExecutor(extensionCodes, scenario, reduceObject, extensionClass, extensionDefinitions);
    }


    /**
     * 构造方法
     *
     * @param extensionCodes       扩展点code
     * @param scenario             场景
     * @param reduceObject         决策对象
     * @param extensionClass       扩展点接口类
     * @param extensionDefinitions 扩展点实现
     */
    public CodeExtensionExecutor(List<String> extensionCodes, String scenario, R reduceObject, Class<E> extensionClass, List<ExtensionDefinition<E, R>> extensionDefinitions) {
        super(scenario, reduceObject, extensionClass, extensionDefinitions);
        this.extensionCodes = extensionCodes;
    }

    @Override
    protected StringBuilder builder(StringBuilder stringBuilder) {
        return stringBuilder;
    }
}
