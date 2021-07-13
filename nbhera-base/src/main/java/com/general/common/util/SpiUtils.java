package com.general.common.util;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author xvanning
 * date: 2021/5/28 0:13
 * desc: Java SPI 扩展点的实现
 */
public class SpiUtils {

    /**
     * 读取spi多实现
     *
     * @param interfaceClass 接口类型
     * @param <T>            接口泛型
     * @return 接口配置的实现
     */
    public static <T> List<T> load(Class<T> interfaceClass) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        ServiceLoader.load(interfaceClass).iterator(), Spliterator.ORDERED),
                false)
                .collect(Collectors.toList());
    }
}
