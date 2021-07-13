package com.general.common.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.common.collect.ImmutableList;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.regex.Pattern;

/**
 * @author xvanning
 * date: 2021/6/20 16:12
 * desc: 全局配置
 */
public class GlobalConfig {

    /**
     * 基础类class
     */
    private static final List<Class<?>> NATIVE_ClASS_LIST;

    static {
        NATIVE_ClASS_LIST = ImmutableList.of(SimpleDateFormat.class,
                Timestamp.class,
                Date.class,
                Time.class,
                Date.class,
                Calendar.class,
                XMLGregorianCalendar.class,
                JSONObject.class,
                JSONArray.class,
                Object.class,
                String.class,
                StringBuilder.class,
                StringBuffer.class,
                Character.class,
                byte.class,
                Byte.class,
                short.class,
                Short.class,
                int.class,
                Integer.class,
                long.class,
                Long.class,
                BigInteger.class,
                BigDecimal.class,
                float.class,
                Float.class,
                double.class,
                Double.class,
                boolean.class,
                Boolean.class,
                Class.class,
                char[].class,
                AtomicBoolean.class,
                AtomicInteger.class,
                AtomicLong.class,
                AtomicReference.class,
                WeakReference.class,
                SoftReference.class,
                UUID.class,
                TimeZone.class,
                Locale.class,
                Currency.class,
                Inet4Address.class,
                Inet6Address.class,
                InetSocketAddress.class,
                File.class,
                URI.class,
                URL.class,
                Pattern.class,
                Charset.class,
                JSONPath.class,
                Number.class,
                AtomicIntegerArray.class,
                AtomicLongArray.class,
                StackTraceElement.class,
                Serializable.class,
                Cloneable.class,
                Comparable.class,
                Closeable.class);

    }

    /**
     * 是否为基础class类
     *
     * @param targetClass 当前目标类
     * @return 是否是基础class类
     */
    public static boolean isNative(Class<?> targetClass) {
        if (NATIVE_ClASS_LIST.contains(targetClass)) {
            return true;
        }
        return Map.class.isAssignableFrom(targetClass) || targetClass.isArray() || Collection.class.isAssignableFrom(targetClass) || Set.class.isAssignableFrom(targetClass);
    }
}
