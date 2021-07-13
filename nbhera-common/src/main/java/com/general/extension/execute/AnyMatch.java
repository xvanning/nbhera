package com.general.extension.execute;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/9 22:33
 * desc: 任意一个符合执行器
 */
public class AnyMatch<T> extends Reducer<T, Boolean> {
    @Nonnull
    private Predicate<T> predicate;

    /**
     * 构造函数
     *
     * @param predicate predicate
     */
    public AnyMatch(@Nonnull Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
        this.setResult(false);
    }

    @Override
    public boolean willBreak(Collection<T> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return false;
        }
        for (T element : elements) {
            if (predicate.test(element)) {
                setResult(true);
                setBreak();
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean reduce(Collection<T> elements) {
        return hasBreak() ? getResult() : false;
    }
}
