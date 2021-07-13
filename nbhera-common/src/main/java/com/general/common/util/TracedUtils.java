package com.general.common.util;

import lombok.Getter;

/**
 * @author xvanning
 * date: 2021/5/29 21:30
 * desc: 调用工具类
 */
public final class TracedUtils {


    /**
     * 当前启动容器是否支持调用链
     */
    @Getter
    private Boolean supportTraced;

    /**
     * 唯一实例
     */
    private static final TracedUtils INSTANCE = new TracedUtils();

    /**
     * 获取唯一实例
     *
     * @return 唯一实例
     */
    public static TracedUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 私有构造方法
     */
    private TracedUtils() {
        try {
//            EagleEye.getRpcContext();
            supportTraced = true;
        } catch (Throwable throwable) {
            // skip
            supportTraced = false;
        }
    }
}
