package com.general.extension.execute;

import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

/**
 * @param <K> key
 * @param <V> value
 * @author xvanning
 * date: 2021/6/20 13:31
 * desc: 收集成map的执行器
 */
public class MapCollect<K, V> extends Reducer<Map<K, V>, Map<K, V>> {
    @Nonnull
    private Predicate<Map<K, V>> predicate;

    /**
     * 构造函数
     *
     * @param predicate predicate
     */
    public MapCollect(@Nonnull Predicate<Map<K, V>> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public boolean willBreak(Collection<Map<K, V>> elements) {
        return false;
    }

    @Override
    public Map<K, V> reduce(Collection<Map<K, V>> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptyMap();
        }
        Map<K, V> result = new HashMap<>(elements.size());
        elements.stream().filter(element -> predicate.test(element)).forEach(result::putAll);
        return result;
    }
}
