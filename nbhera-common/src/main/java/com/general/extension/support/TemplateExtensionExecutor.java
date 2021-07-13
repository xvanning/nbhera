package com.general.extension.support;

import com.general.nbhera.extension.ExtensionPoints;

import java.util.List;

/**
 * @param <E> 扩展点泛型
 * @param <R> 扩展点决策对象泛型
 * @author xvanning
 * date: 2021/6/6 21:54
 * desc: 模板模式执行器
 */
public class TemplateExtensionExecutor<E extends ExtensionPoints<E, R>, R> extends ExtensionExecutor<E, R> {


    /**
     * session id
     */
    private String sessionId;

    /**
     * 业务code
     */
    private String bizCode;

    /**
     * 构造扩展点执行器
     *
     * @param sessionId      sessionId
     * @param bizCode        业务code
     * @param scenario       场景code
     * @param reduceTarget   决策对象
     * @param extensionClass 扩展点类
     * @param <E>            扩展点泛型
     * @param <R>            扩展点决策对象泛型
     * @return 扩展点执行器
     */
    public static <E extends ExtensionPoints<E, R>, R> TemplateExtensionExecutor of(String sessionId, String bizCode, String scenario, R reduceTarget, Class<E> extensionClass) {
        List<ExtensionDefinition<E, R>> extensionPoints = ExtensionRegistryCenter.getExtensionDefinitionList(bizCode, scenario, reduceTarget, extensionClass);
        return new TemplateExtensionExecutor(sessionId, bizCode, scenario, reduceTarget, extensionClass, extensionPoints);
    }


    /**
     * 构造方法
     *
     * @param sessionId            sessionId
     * @param bizCode              业务code
     * @param scenario             场景
     * @param reduceTarget         决策对象
     * @param extensionClass       扩展点接口类
     * @param extensionDefinitions 扩展点实现
     */
    protected TemplateExtensionExecutor(String sessionId, String bizCode, String scenario, R reduceTarget, Class<E> extensionClass, List<ExtensionDefinition<E, R>> extensionDefinitions) {
        super(scenario, reduceTarget, extensionClass, extensionDefinitions);
        this.sessionId = sessionId;
        this.bizCode = bizCode;
    }


    @Override
    protected StringBuilder builder(StringBuilder stringBuilder) {
        return stringBuilder.append(sessionId).append(SEPARATOR).append(bizCode).append(SEPARATOR);
    }
}
