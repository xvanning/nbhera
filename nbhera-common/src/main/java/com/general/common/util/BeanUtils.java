package com.general.common.util;

/**
 * @author xvanning
 * date: 2021/6/8 23:29
 * desc: 类工具
 */

public class BeanUtils {

    /**
     * 根据 class 创建 类实例
     *
     * @param beanClass beanClass
     * @param <T>       泛型
     * @return 类实例
     */
    public static <T> T createBeanInstance(Class<T> beanClass) {
        try {
            return beanClass.newInstance();
        } catch (Throwable throwable) {
            return null;
        }
    }
}
