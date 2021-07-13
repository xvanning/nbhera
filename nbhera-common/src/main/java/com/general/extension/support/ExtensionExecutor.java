package com.general.extension.support;

import com.general.common.exception.SystemException;
import com.general.common.util.AssertUtils;
import com.general.constants.SystemCode;
import com.general.extension.execute.Reducer;
import com.general.extension.Reducers;
import com.general.nbhera.extension.ExtensionPoints;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <E>            扩展点真实类
 * @param <ReduceTarget> 决策对象泛型
 * @author xvanning
 * date: 2021/6/3 21:52
 * desc: 扩展点执行器
 */
@Slf4j
@Getter
public abstract class ExtensionExecutor<E extends ExtensionPoints<E, ReduceTarget>, ReduceTarget> {

    /**
     * reduce target thread local
     */
    protected static final ThreadLocal<Object> REDUCE_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 分隔符
     */
    protected static final String SEPARATOR = "|";

    /**
     * 场景
     */
    private String scenario;

    /**
     * 决策对象
     */
    private ReduceTarget reduceTarget;

    /**
     * 扩展点类
     */
    private Class<E> extensionClass;

    /**
     * 决策出的扩展点实现类
     */
    protected List<ExtensionDefinition<E, ReduceTarget>> extensionPoints;

    /**
     * 构造方法
     *
     * @param scenario             场景
     * @param reduceTarget         决策对象
     * @param extensionClass       扩展点接口类
     * @param extensionDefinitions 扩展点实现
     */
    protected ExtensionExecutor(String scenario, ReduceTarget reduceTarget, Class<E> extensionClass, List<ExtensionDefinition<E, ReduceTarget>> extensionDefinitions) {
        if (CollectionUtils.isEmpty(extensionPoints)) {
            throw new SystemException(SystemCode.EXTENSION, "决策后扩展点实现为空").addExtension("type", getType()).addExtension("extensionClass", extensionClass).addExtension("reduceTarget", reduceTarget);
        }
        this.scenario = scenario;
        this.reduceTarget = reduceTarget;
        this.extensionClass = extensionClass;
        this.extensionPoints = extensionDefinitions;
    }

    /**
     * 获取扩展点实现列表
     *
     * @return 扩展点实现列表
     */
    public List<E> getExtensionPoints() {
        return extensionPoints.stream().map(extensionDefinition -> extensionDefinition.getExtensionPoint()).collect(Collectors.toList());
    }

    /**
     * 获取第一个扩展点实现
     *
     * @return 第一个扩展点
     */
    public E getExtensnionPoint() {
        return extensionPoints.stream().map(extensionDefinition -> extensionDefinition.getExtensionPoint()).findFirst().orElse(null);
    }

    /**
     * 执行第一个
     *
     * @param callBack 回调方法
     * @param <R>      回调方法返回值泛型
     * @param <T>      结果泛型
     * @return 第一个扩展点执行结果
     */
    public final <R, T> R executeFirst(ExtensionCallBack<E, T> callBack) {
        return (R) execute(callBack, Reducers.firstOf());
    }


    /**
     * 执行所有扩展点
     *
     * @param callBack 回调方法
     * @param <R>      回调方法返回值泛型
     * @param <T>      结果泛型
     * @return 所有扩展点执行结果
     */
    public final <R, T> List<R> executeAll(ExtensionCallBack<E, T> callBack) {
        return (List<R>) execute(callBack, Reducers.none());
    }

    /**
     * 日志内容
     *
     * @param stringBuilder stringBuilder
     * @return 日志内容
     */
    protected abstract StringBuilder builder(StringBuilder stringBuilder);

    /**
     * 返回执行器类型
     *
     * @return 执行器类型
     */
    protected String getType() {
        return this.getClass().getSimpleName();
    }


    /**
     * 扩展点执行
     *
     * @param callBack 回调
     * @param reducer  决策方式
     * @param <R>      扩展点返回值泛型
     * @param <T>      决策后返回值泛型
     * @return 扩展点执行结果
     */
    public final <R, T> R execute(ExtensionCallBack<E, T> callBack, Reducer<T, R> reducer) {
        AssertUtils.isNotNull(callBack, SystemCode.EXTENSION, "need set callBack");
        StringBuilder stringBuilder = new StringBuilder("|EXT_EXE|").append(getType()).append(SEPARATOR).append(extensionClass.getSimpleName()).append(reducer.getClass().getSimpleName()).append(SEPARATOR);
        StringBuilder builder = builder(stringBuilder);
        List<T> runResults = Lists.newArrayList();
        for (ExtensionDefinition<E, ReduceTarget> extensionPoint : this.extensionPoints) {
            builder.append(extensionPoint.getCode()).append(SEPARATOR);
            runResults.add(callBack.apply(extensionPoint.getExtensionPoint()));
            if (reducer.willBreak(runResults)) {
                break;
            }
        }
        // 调用日志
        log.info(builder.toString());
        // 返回决策后结果
        return reducer.reduce(runResults);
    }
}
