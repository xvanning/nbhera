package com.general.extension.support;

import com.general.nbhera.extension.ExtensionPoints;

import java.util.function.Function;

/**
 * @param <T> 扩展点
 * @param <R> 返回值
 * @author xvanning
 * date: 2021/6/5 22:15
 * desc: 扩展点回调接口
 */
public interface ExtensionCallBack<T extends ExtensionPoints, R> extends Function<T, R> {
}
