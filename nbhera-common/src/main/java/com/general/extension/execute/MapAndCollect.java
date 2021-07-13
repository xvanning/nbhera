package com.general.extension.execute;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @param <T> key
 * @param <R> value
 * @author xvanning
 * date: 2021/6/20 13:13
 * desc: 收集成map的执行器
 */
public class MapAndCollect<T, R> extends Reducer<T, List<R>> {
    @Nonnull
    private Predicate<T> predicate;

    @Nonnull
    private Function<T, R> mapper;

    /**
     * map and collect
     *
     * @param predicate predicate
     * @param mapper    mapper
     */
    public MapAndCollect(@Nonnull Predicate<T> predicate, @Nonnull Function<T, R> mapper) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
        this.mapper = mapper;
    }


    @Override
    public boolean willBreak(Collection<T> elements) {
        return false;
    }

    @Override
    public List<R> reduce(Collection<T> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptyList();
        }
        return elements.stream().filter(element -> predicate.test(element)).map(element -> mapper.apply(element)).collect(Collectors.toList());
    }
}
