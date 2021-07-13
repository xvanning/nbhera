package com.general.extension.execute;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/9 22:33
 * desc: list 收集成一个的执行器
 */
public class Collect<T> extends Reducer<T, List<T>> {

    @Nonnull
    private Predicate<T> predicate;

    /**
     * 构造函数
     *
     * @param predicate predicate
     */
    public Collect(@Nonnull Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }


    @Override
    public boolean willBreak(Collection<T> elements) {
        return false;
    }

    @Override
    public List<T> reduce(Collection<T> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptyList();
        }
        return elements.stream().filter(element -> predicate.test(element)).collect(Collectors.toList());
    }
}
