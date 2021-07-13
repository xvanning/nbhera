package com.general.extension.execute;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/20 13:40
 * desc: 一个都不符合的执行器
 */
public class NoneMatch<T> extends Reducer<T, Boolean> {

    @Nonnull
    private Predicate<T> predicate;

    /**
     * 构造函数
     *
     * @param predicate predicate
     */
    public NoneMatch(@Nonnull Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
        this.setResult(true);
    }

    @Override
    public boolean willBreak(Collection<T> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return true;
        }
        for (T element : elements) {
            if (predicate.test(element)) {
                this.setBreak();
                this.setResult(false);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean reduce(Collection<T> elements) {
        return hasBreak() ? getResult() : true;
    }
}
