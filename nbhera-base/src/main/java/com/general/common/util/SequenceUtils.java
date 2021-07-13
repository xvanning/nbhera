package com.general.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author xvanning
 * date: 2021/5/27 0:10
 * desc: 序列工具，包括唯一id生成，序列生成，唯一过滤
 */
public class SequenceUtils {

    private static Map<String, AtomicLong> sequenceSourceMap = new ConcurrentHashMap<>(16);

    /**
     * 对指定key， 获取下一个序列
     *
     * @param key key
     * @return 序列
     */
    public static long next(String key) {
        AtomicLong sequence = sequenceSourceMap.get(key);
        if (null == sequence) {
            synchronized (SequenceUtils.class) {
                sequence = sequenceSourceMap.get(key);
                if (null == sequence) {
                    sequence = new AtomicLong();
                    sequenceSourceMap.put(key, sequence);
                }
            }
        }
        return sequence.addAndGet(1);
    }

    /**
     * 根据uuid获取唯一id， 去除 "-"
     *
     * @return 唯一id
     */
    public static String generateUniqueId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    /**
     * 根据某个key来进行过滤
     *
     * @param keyExtractor keyExtractor
     * @param <T>          返回类型
     * @return 是否成功过滤
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 根据唯一id和分库分表键 来生成唯一id，
     * 后面可以同步解析
     *
     * @param sequence 唯一id
     * @param shareId  分片id
     * @return 唯一id
     */
    public static Long generateUniqueId(Long sequence, Long shareId) {
        long tableIdx = shareId % 512;
        return (sequence << 10) | tableIdx;
    }

    /**
     * 根据上个方法生成的唯一id，获取到分库分表的表id
     *
     * @param uniqueId 唯一id
     * @return map
     */
    public static Map<String, Object> getMap(Long uniqueId) {
        Map<String, Object> map = new HashMap<>();
        map.put("uniqueId", uniqueId);
        map.put("tableIdx", uniqueId & 511);
        /*+TDDL({"type":"condition","vtab":"table_name","params":[{"expr":["user_id=#{tableIdx}:long"]}]})*/
        return map;
    }


}
