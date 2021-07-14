package com.general.extension.execute;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/20 13:06
 * desc: 第一个执行器
 */
public class FirstOf<T> extends Reducer<T, T> {
    @Nonnull
    private Predicate<T> predicate;

    public FirstOf() {

    }

    /**
     * 构造函数
     *
     * @param predicate predicate
     */
    public FirstOf(@Nonnull Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public boolean willBreak(Collection<T> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return false;
        }
        for (T element : elements) {
            if (null == predicate || predicate.test(element)) {
                setResult(element);
                setBreak();
                return true;
            }
        }
        return false;
    }

    @Override
    public T reduce(Collection<T> elements) {
        if (hasBreak()) {
            return getResult();
        }
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        for (T element : elements) {
            if (predicate.test(element)) {
                return element;
            }
        }
        return null;
    }
}
