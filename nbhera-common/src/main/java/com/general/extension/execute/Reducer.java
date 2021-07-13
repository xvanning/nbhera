package com.general.extension.execute;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * @param <T> 请求参数泛型
 * @param <R> 返回值泛型
 * @author xvanning
 * date: 2021/6/5 22:02
 * desc: 决策器抽象类
 */

public abstract class Reducer<T, R> {

    private boolean hasBreak = false;

    @Getter
    @Setter
    private R result;

    /**
     * 进行中断
     */
    public final void setBreak() {
        this.hasBreak = true;
    }

    /**
     * 是否中断
     *
     * @return 是否中断
     */
    public final boolean hasBreak() {
        return hasBreak;
    }

    /**
     * 是否将要终止
     *
     * @param elements elements
     * @return 是否终止
     */
    public abstract boolean willBreak(Collection<T> elements);

    /**
     * 决策
     *
     * @param elements elements
     * @return 决策
     */
    public abstract R reduce(Collection<T> elements);

}
