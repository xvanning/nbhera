package com.general.extension.support;

import com.general.common.exception.SystemException;
import com.general.constants.SystemCode;
import com.general.extension.execute.Reducer;
import com.general.extension.Reducers;
import com.general.extension.annotation.Reduce;
import com.general.extension.enums.ReduceType;
import com.general.nbhera.extension.ExtensionPoints;
import com.general.nbhera.extension.aonnotation.Strategy;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @author xvanning
 * date: 2021/6/3 21:59
 * desc: 扩展点代理对象
 */
@Slf4j
public class ExtensionProxy {

    /**
     * 对象缓存，key 接口， value 代理对象
     */
    private static final Map<Class<?>, ExtensionPoints> CACHE = Maps.newConcurrentMap();

    /**
     * 决策代理类对象缓存
     */
    private static final Map<Class<?>, ExtensionPoints> REDUCE_PROXY_CACHE = Maps.newConcurrentMap();

    /**
     * 方法调用模式缓存， key 方法， value 调用模式
     */
    private static final Map<Method, ReduceType> REDUCE_CACHE = Maps.newConcurrentMap();

    /**
     * 根据扩展点接口类返回代理对象
     *
     * @param targetClass 扩展点目标接口类
     * @param <E>         泛型，表示接口类
     * @return 代理类
     */
    public static <E extends ExtensionPoints> E getProxyInstance(Class<E> targetClass) {
        return (E) CACHE.computeIfAbsent(targetClass, value -> createProxy(targetClass));
    }

    /**
     * 创建代理对象
     *
     * @param targetClass 扩展点目标接口类
     * @param <E>         泛型
     * @return 代理类
     */
    private static <E extends ExtensionPoints> E createProxy(Class<E> targetClass) {
        log.info("|EXT_PROXY| ExtensionProxy proxy = {}", targetClass);
        // 遍历所有public方法，启动时要加载缓存
        Method[] methods = targetClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            REDUCE_CACHE.computeIfAbsent(method, value -> getReduceTypeByMethod(method));
        }

        // 同时创建扩展点决策类代理
        reduceProxy(targetClass);

        // 接口 xxxExt 加载到spring的是当前的动态代理对象
        // 如果是进行了 reduce 这边会创建第二个动态代理对象
        // Object xxxExt = xxxExt.reduce(targetObject)  第二个动态代理对象
        // xxxExt.realMethod();  第二个动态代理的方法
        return (E) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[]{targetClass}, (proxy, method, args) -> {
            // 如果是Object的方法，则不需要代理，包括toString、hashCode、equals等
            if (method.getDeclaringClass() == Object.class) {
                if (method.getName().equals("toString")) {
                    return targetClass.getName() + "$reduce.proxy";
                }
                return method.invoke(proxy, args);
            }
            // 如果是扩展点的方法，则特殊处理
            if (method.getDeclaringClass() == ExtensionPoints.class) {
                if (method.getName().equals("reduce")) {
                    if (args != null && args.length == 1) {
                        ExtensionExecutor.REDUCE_THREAD_LOCAL.set(args[0]);
                    }
                    return reduceProxy(targetClass);
                }
                if (method.getName().equals("reduceByCode")) {
                    String template = String.valueOf(args[0]);
                    String scenario = args.length == 1 ? null : String.valueOf(args[1]);
                    return ExtensionRegistryCenter.getExtensionPoint(template, scenario, targetClass);
                }
            }
            return reduce(targetClass, null, proxy, method, args);
        });
    }

    /**
     * 创建决策代理对象
     *
     * @param targetClass 扩展点目标接口类
     * @param <E>         泛型
     * @return 代理类
     */
    private static <E extends ExtensionPoints> E reduceProxy(Class<E> targetClass) {
        return (E) REDUCE_PROXY_CACHE.computeIfAbsent(targetClass, value -> (E) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[]{targetClass}, (proxy, method, args) -> {
            try {
                // 如果是Object的方法，则不需要代理，包括toString、hashCode、equals等
                if (method.getDeclaringClass() == Object.class) {
                    if (method.getName().equals("toString")) {
                        return targetClass.getName() + "$reduce.proxy";
                    }
                    return method.invoke(proxy, args);
                }
                // 如果是扩展点的方法，则特殊处理，不能 反复reduce
                if (method.getDeclaringClass() == ExtensionPoints.class) {
                    throw new SystemException(SystemCode.EXTENSION, "not support!");
                }
                return reduce(targetClass, ExtensionExecutor.REDUCE_THREAD_LOCAL.get(), proxy, method, args);
            } finally {
                ExtensionExecutor.REDUCE_THREAD_LOCAL.remove();
            }

        }));
    }

    /**
     * 代理内部执行方法
     *
     * @param targetClass  扩展点类
     * @param reduceTarget 决策对象
     * @param proxy        代理对象
     * @param method       方法
     * @param args         参数
     * @param <E>          泛型
     * @return 扩展点的执行方法
     */
    private static <E extends ExtensionPoints> Object reduce(Class<E> targetClass, Object reduceTarget, Object proxy, Method method, Object[] args) {
        // 决策类型
        ReduceType reduceType = REDUCE_CACHE.computeIfAbsent(method, value -> getReduceTypeByMethod(method));
        // 确定执行器
        ExtensionExecutor extensionExecutor = targetClass.isAnnotationPresent(Strategy.class) ? Reducers.strategy(reduceTarget, targetClass) : Reducers.reduce(reduceTarget, targetClass);
        // 根据决策调用
        return extensionExecutor.execute(extension -> {
            try {
                return method.invoke(extension, args);
            } catch (RuntimeException e) {
                throw e;
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                log.error("Method invoke failed. target = {}, method = {}", targetClass, method);
                throw new RuntimeException("Method invoke failed. " + e.getMessage());
            } catch (Exception e) {
                log.error("Method invoke failed. target = {}, method = {}", targetClass, method);
                throw new RuntimeException("Method invoke failed. " + e.getMessage());
            }
        }, getReducerByReduceType(reduceType));
    }

    /**
     * 根据决策策略返回决策器
     *
     * @param reduceType 决策类型
     * @param <T>        请求参数泛型
     * @param <R>        返回值泛型
     * @return 决策器，具体到子类
     */
    private static <T, R> Reducer<T, R> getReducerByReduceType(ReduceType reduceType) {
        switch (reduceType) {
            case FIRST:
                return (Reducer<T, R>) Reducers.firstOf();
            case ANY_MATCH:
                return (Reducer<T, R>) Reducers.anyMatch(r -> (Boolean) r);
            case ALL_MATCH:
                return (Reducer<T, R>) Reducers.allMatch(r -> (Boolean) r);
            case LIST:
                return (Reducer<T, R>) Reducers.flatCollect(r -> r != null);
            case MAP:
                return (Reducer<T, R>) Reducers.mapCollect(r -> r != null);
            default:
                throw new SystemException(SystemCode.EXTENSION, "can not support reduceType = " + reduceType);
        }
    }

    /**
     * @param method 方法
     * @return 决策策略
     */
    private static ReduceType getReduceTypeByMethod(Method method) {
        Reduce reduce = AnnotationUtils.getAnnotation(method, Reduce.class);
        if (null == reduce || !reduce.value().getSupport()) {
            return ReduceType.FIRST;
        }
        // 支持合并的几种方法
        ReduceType reduceType = reduce.value();
        Class<?> returnType = method.getReturnType();

        String error = null;

        if (reduceType == ReduceType.ANY_MATCH || reduceType == ReduceType.ALL_MATCH) {
            // 此时返回值必须是布尔值
            if (returnType != Boolean.class && returnType != boolean.class) {
                error = method.toString() + " reduceType = " + reduceType + ", but return type is not Boolean/boolean";
            }
        } else if (reduceType == ReduceType.MAP) {
            // 此时返回值必须是 Map
            if (returnType != Map.class) {
                error = method.toString() + " reduceType = " + reduceType + ", but return type is not Map";
            }
        } else if (reduceType == ReduceType.LIST) {
            // 此时返回值必须是 List
            if (returnType != List.class) {
                error = method.toString() + " reduceType = " + reduceType + ", but return type is not List";
            }
        }
        if (StringUtils.isNotBlank(error)) {
            throw new SystemException(SystemCode.EXTENSION, error);
        }
        return reduceType;
    }
}
