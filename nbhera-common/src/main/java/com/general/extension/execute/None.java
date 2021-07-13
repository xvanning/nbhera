package com.general.extension.execute;

import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/6 21:00
 * desc: 不执行任何决策器
 */
@NoArgsConstructor
public class None<T> extends Reducer<T, List<T>> {


    @Override
    public boolean willBreak(Collection<T> elements) {
        return false;
    }

    @Override
    public List<T> reduce(Collection<T> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(elements.size());
        result.addAll(elements);
        return result;
    }
}
