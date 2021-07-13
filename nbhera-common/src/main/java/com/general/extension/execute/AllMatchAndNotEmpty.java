package com.general.extension.execute;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/9 22:33
 * desc: 全部符合并且不为空执行器
 */
public class AllMatchAndNotEmpty<T> extends AllMatch<T> {


    /**
     * allMatch
     *
     * @param predicate predicate
     */
    public AllMatchAndNotEmpty(@Nonnull Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    public Boolean reduce(Collection<T> elements) {
        if (hasBreak()) {
            return getResult();
        }
        return CollectionUtils.isNotEmpty(elements);
    }
}
