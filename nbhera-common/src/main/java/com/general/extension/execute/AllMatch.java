package com.general.extension.execute;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/9 22:32
 * desc: 全部符合执行器
 */
public class AllMatch<T> extends Reducer<T, Boolean> {
    @Nonnull
    private Predicate<T> predicate;

    /**
     * allMatch
     *
     * @param predicate predicate
     */
    public AllMatch(@Nonnull Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
        this.setResult(false);
    }

    /**
     * 获取 predicate
     *
     * @return predicate
     */
    protected Predicate<T> getPredicate() {
        return predicate;
    }

    @Override
    public boolean willBreak(Collection<T> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return false;
        }
        for (T element : elements) {
            if (!predicate.test(element)) {
                setResult(false);
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
