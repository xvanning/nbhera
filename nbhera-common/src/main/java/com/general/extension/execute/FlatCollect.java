package com.general.extension.execute;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/20 13:09
 * desc: 打散的集合执行器
 */
public class FlatCollect<T> extends Reducer<List<T>, List<T>> {

    @Nonnull
    private Predicate<List<T>> predicate;

    /**
     * 构造函数
     *
     * @param predicate predicate
     */
    public FlatCollect(@Nonnull Predicate<List<T>> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }


    @Override
    public boolean willBreak(Collection<List<T>> elements) {
        return false;
    }

    @Override
    public List<T> reduce(Collection<List<T>> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptyList();
        }
        List<T> results = Lists.newArrayList();
        elements.stream().filter(element -> CollectionUtils.isNotEmpty(element) && predicate.test(element)).forEach(results::addAll);
        return results;
    }
}
