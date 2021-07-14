package com.general.extension;

import com.general.extension.execute.*;
import com.general.extension.session.BizSession;
import com.general.extension.support.CodeExtensionExecutor;
import com.general.extension.support.ExtensionExecutor;
import com.general.extension.support.StrategyExtensionExecutor;
import com.general.extension.support.TemplateExtensionExecutor;
import com.general.nbhera.extension.ExtensionPoints;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author xvanning
 * date: 2021/5/30 19:32
 * desc:
 */
@Slf4j
public class Reducers {

    /**
     * 不做任何reduce
     *
     * @param <T> 结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<T, List<T>> none() {
        return new None<>();
    }


    /**
     * 找到第一个满足条件的结果
     *
     * @param predicate predicate
     * @param <T>       结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<T, T> firstOf(Predicate<T> predicate) {
        return new FirstOf<>(predicate);
    }

    /**
     * 找到第一个满足条件的结果
     *
     * @param <T> 结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<T, T> firstOf() {
        return new FirstOf<>();
    }

    /**
     * 是否任意一个元素满足一定条件，如果所有元素都为空，则返回false
     *
     * @param predicate predicate
     * @param <T>       结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<T, Boolean> allMatch(@NonNull Predicate<T> predicate) {
        return new AllMatch<>(predicate);
    }

    /**
     * 是否所有元素都满足一定条件，如果所有元素都为空，则返回false
     *
     * @param predicate predicate
     * @param <T>       结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<T, Boolean> allMatchAndNotEmpty(@NonNull Predicate<T> predicate) {
        return new AllMatchAndNotEmpty<>(predicate);
    }

    /**
     * 是否任意一个元素满足一定条件，如果所有元素都为空，则返回false
     *
     * @param predicate predicate
     * @param <T>       结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<T, Boolean> anyMatch(@NonNull Predicate<T> predicate) {
        return new AnyMatch<>(predicate);
    }

    /**
     * 是否任意所有元素满足一定条件，如果所有元素都为空，则返回false
     *
     * @param predicate predicate
     * @param <T>       结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<T, Boolean> noneMacth(@NonNull Predicate<T> predicate) {
        return new NoneMatch<>(predicate);
    }


    /**
     * 满足条件的集合被拼在一起，进行展开，如果没有预期结果则返回一个空list
     *
     * @param predicate predicate
     * @param <T>       结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<List<T>, List<T>> flatCollect(@NonNull Predicate<List<T>> predicate) {
        return new FlatCollect<>(predicate);
    }


    /**
     * 满足条件的集合被拼在一起，如果没有预期结果则返回一个空list
     *
     * @param predicate predicate
     * @param <T>       结果值泛型
     * @return 决策器
     */
    public static <T> Reducer<T, List<T>> collect(@NonNull Predicate<T> predicate) {
        return new Collect<>(predicate);
    }

    /**
     * 满足条件的集合被拼在一起，进行展开，如果没有预期结果则返回一个空list
     *
     * @param predicate predicate
     * @param <K>       map的key
     * @param <V>       map的value
     * @return 决策器
     */
    public static <K, V> Reducer<Map<K, V>, Map<K, V>> mapCollect(@NonNull Predicate<Map<K, V>> predicate) {
        return new MapCollect<>(predicate);
    }

    /**
     * 策略模式使用扩展点，要求扩展点定义类上存在 @Strategy 注解
     *
     * @param reduceObject 决策对象
     * @param targetClass  扩展点接口类
     * @param <E>          扩展点泛型
     * @param <R>          决策对象泛型
     * @return 扩展点执行器
     */
    public static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor strategy(R reduceObject, Class<E> targetClass) {
        return strategy(null, reduceObject, targetClass);
    }

    /**
     * 策略模式使用扩展点，要求扩展点定义类上存在@Strategy注解
     *
     * @param scenario     场景
     * @param reduceObject 决策对象
     * @param targetClass  扩展点类
     * @param <R>          决策对象泛型
     * @param <E>          扩展点泛型
     * @return 扩展点执行器
     */
    public static <R, E extends ExtensionPoints<E, R>> ExtensionExecutor strategy(String scenario, R reduceObject, Class<E> targetClass) {
        return StrategyExtensionExecutor.of(scenario, reduceObject, targetClass);
    }

    /**
     * 根据业务code、场景和决策对象决策出扩展点实现类
     *
     * @param targetClass 扩展点接口类
     * @param <E>         扩展点泛型
     * @param <R>         决策对象泛型
     * @return 决策出的扩展点实现
     */
    public static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor<E, R> reduce(Class<E> targetClass) {
        return reduce(null, targetClass);
    }

    /**
     * 根据业务code、场景和决策对象决策出扩展点实现类
     *
     * @param reduceTarget 决策对象
     * @param targetClass  扩展点接口类
     * @param <E>          扩展点泛型
     * @param <R>          决策对象泛型
     * @return 决策出的扩展点实现
     */
    public static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor<E, R> reduce(R reduceTarget, Class<E> targetClass) {
        BizSession session = BizSession.currentSession();
        return TemplateExtensionExecutor.of(session.getSessionId(), session.getBizCode(), session.getScenario(), reduceTarget, targetClass);
    }

    /**
     * 根据业务code、场景和决策对象决策出扩展点实现类
     *
     * @param bizCode      业务code
     * @param scenario     场景code
     * @param reduceTarget 决策对象
     * @param targetClass  扩展点类
     * @param <E>          扩展点泛型
     * @param <R>          决策对象泛型
     * @return 决策出的扩展点实现
     */
    public static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor<E, R> reduce(String bizCode, String scenario, R reduceTarget, Class<E> targetClass) {
        return TemplateExtensionExecutor.of(null, bizCode, scenario, reduceTarget, targetClass);
    }

    /**
     * 根据模板code、决策对象决策出扩展点实现类，决策对象为null，场景code为null
     *
     * @param templateCode 模板code
     * @param targetClass  扩展点类
     * @param <E>          扩展点泛型
     * @param <R>          决策对象泛型
     * @return 决策出的扩展点实现
     */
    public static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor<E, R> reduceByCode(String templateCode, Class<E> targetClass) {
        return reduceByCodes(Lists.newArrayList(templateCode), null, null, targetClass);
    }

    /**
     * 根据模板code、场景code决策出扩展点实现类，决策对象为null
     *
     * @param templateCode 模板code
     * @param scenario     场景code
     * @param targetClass  扩展点类
     * @param <E>          扩展点泛型
     * @param <R>          决策对象泛型
     * @return 决策出的扩展点实现
     */
    public static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor<E, R> reduceByCode(String templateCode, String scenario, Class<E> targetClass) {
        return reduceByCodes(Lists.newArrayList(templateCode), scenario, null, targetClass);
    }

    /**
     * 根据模板code决策出扩展点实现类，决策对象为null，场景code为null
     *
     * @param templateCodes 模板codes
     * @param targetClass   扩展点类
     * @param <E>           扩展点泛型
     * @param <R>           决策对象泛型
     * @return 决策出的扩展点实现
     */
    public static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor<E, R> reduceByCode(List<String> templateCodes, Class<E> targetClass) {
        return reduceByCodes(templateCodes, null, null, targetClass);
    }

    /**
     * 根据模板code决策出扩展点实现类，决策对象为null，场景code为null
     *
     * @param templateCodes 模板codes
     * @param reduceTarget  决策对象
     * @param targetClass   扩展点类
     * @param <E>           扩展点泛型
     * @param <R>           决策对象泛型
     * @return 决策出的扩展点实现
     */
    public static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor<E, R> reduceByCode(List<String> templateCodes, R reduceTarget, Class<E> targetClass) {
        return reduceByCodes(templateCodes, null, reduceTarget, targetClass);
    }

    /**
     * 根据模板code、场景和决策对象决策出扩展点实现类
     *
     * @param templateCodes 模板code
     * @param scenario      场景code
     * @param reduceTarget  决策对象
     * @param targetClass   扩展点类
     * @param <E>           扩展点泛型
     * @param <R>           决策对象泛型
     * @return 决策出的扩展点实现
     */
    private static <E extends ExtensionPoints<E, R>, R> ExtensionExecutor<E, R> reduceByCodes(List<String> templateCodes, String scenario, R reduceTarget, Class<E> targetClass) {
        return CodeExtensionExecutor.of(templateCodes, scenario, reduceTarget, targetClass);
    }

}
