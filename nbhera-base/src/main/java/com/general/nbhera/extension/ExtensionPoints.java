package com.general.nbhera.extension;

/**
 * @param <T>            接口子类
 * @param <ReduceTarget> 决策对象
 * @author xvanning
 * date: 2021/5/14 0:04
 * desc: 扩展点定义
 */
public interface ExtensionPoints<T extends ExtensionPoints, ReduceTarget> {

    /**
     * 这里是为了兼容代理模式，可以通过代理设置决策对象，当前方法在非代理类调用无效
     *
     * @param reduceTarget 决策对象
     * @return 当前对象
     */
    default T reduce(ReduceTarget reduceTarget) {
        throw new RuntimeException("NOT SUPPORT");
    }

    /**
     * 这里是为了兼容代理模式，可以通过代理设置决策模板code，当前方法在非代理类调用无效
     *
     * @param templateCode 模板code
     * @return 当前对象
     */
    default T reduceByCode(String templateCode) {
        throw new RuntimeException("NOT SUPPORT");
    }

    /**
     * 这里是为了兼容代理模式，可以通过代理设置决策模板code，当前方法在非代理类调用无效
     *
     * @param templateCode 模板code
     * @param scenarioCode 场景code
     * @return 当前对象
     */
    default T reduceByCode(String templateCode, String scenarioCode) {
        throw new RuntimeException("NOT SUPPORT");
    }

    /**
     * 根据决策对象返回，当前扩展点是否支持
     *
     * @param reduceTarget 决策对象
     * @return 是否支持
     */
    default boolean support(ReduceTarget reduceTarget) {
        return true;
    }
}
