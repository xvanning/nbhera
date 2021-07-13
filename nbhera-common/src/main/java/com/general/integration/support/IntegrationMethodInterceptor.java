package com.general.integration.support;

import com.alibaba.fastjson.JSON;
import com.general.common.exception.SystemException;
import com.general.common.exception.ThirdException;
import com.general.common.exception.WithCodeException;
import com.general.integration.annotation.NbClientLog;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author xvanning
 * date: 2021/6/23 23:01
 * desc: 三方接口aop处理
 */
@Slf4j
public class IntegrationMethodInterceptor implements MethodInterceptor {

    /**
     * key - 代理method， value - 带有@NbClient注解的真实方法
     */
    private static final ConcurrentMap<Method, MethodConfig> INTERFACE_METHOD_CACHE = Maps.newConcurrentMap();

    @Override

    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 计时器
        Stopwatch stopwatch = Stopwatch.createStarted();

        // 获取方法声明 class 的 @NbClient 接口对应的方法声明
        MethodConfig methodConfig = this.getAnnotationMethodConfig(invocation);

        // 过程参数
        WithCodeException exception = null;
        Object result = null;
        boolean success = false;

        try {
            result = invocation.proceed();
            success = true;
        } catch (WithCodeException withCodeException) {
            exception = withCodeException;
        } catch (Throwable originThrowable) {
            exception = new ThirdException("THIRD_EXCEPTION", methodConfig.getMethodSign() + "failed!", originThrowable);
        }

        // 接口执行成功
        if (success) {
            log.info("|INTEGRATION[{}][true][{}ms]|, request = {}, response = {}", methodConfig.getMethodSign(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS), JSON.toJSONString(invocation.getArguments()), JSON.toJSONString(result));
        }

        // 接口执行失败
        // 装配错误信息
        Map<String, Object> request = Maps.newHashMap();
        if (methodConfig.getParamNames() != null && methodConfig.getParamNames().length > 0) {
            Object[] args = invocation.getArguments();
            for (int index = 0; index < methodConfig.getParamNames().length; index++) {
                request.put(methodConfig.getParamNames()[index], args[index]);
            }
        }
        exception.addExtension("method", methodConfig.getMethodSign()).addExtension("args", JSON.toJSONString(request));
        log.error("|INTEGRATION [{}][false][{}ms]| args = {}, extension = {}", methodConfig.getMethodSign(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS), JSON.toJSONString(request), JSON.toJSONString(exception.getExtension()));

        // 是否要继续抛出异常
        if (methodConfig.nbClientLog.needThrow()) {
            throw exception;
        } else {
            // 如果不继续抛出，则记录原来的异常
            log.error("|INTEGRATION-EX [" + methodConfig.getMethod() + "]", exception);
        }

        if (StringUtils.isBlank(methodConfig.getNbClientLog().defaultRet())) {
            return null;
        }

        // 返回默认值
        return this.getDefaultReturn(methodConfig.getMethod(), methodConfig.getNbClientLog().defaultRet());
    }

    /**
     * 获取方法声明 class 的 @NbClientLog 接口对应的方法相关配置参数
     *
     * @param invocation 代理对象
     * @return 带注解的方法
     */
    private MethodConfig getAnnotationMethodConfig(MethodInvocation invocation) {
        // 获取当前aop的方法
        Method targetMethod = invocation.getMethod();
        return INTERFACE_METHOD_CACHE.computeIfAbsent(targetMethod, value -> {
            // 获取方法，优先从接口中获取
            Method method = this.getAnnotationMethod(targetMethod);
            // 获取注解
            NbClientLog nbClientLog = method.getAnnotation(NbClientLog.class);
            // 获取方法参数名
            String[] paramNames = this.getParamNames(method);
            return new MethodConfig(method, nbClientLog, paramNames);
        });
    }

    /**
     * 根据接口获取 返回带注解的真是方法
     *
     * @param method 方法
     * @return 带注解的真实方法
     */
    private Method getAnnotationMethod(Method method) {

        try {
            // 优先从接口中获取
            Class<?>[] interfaceClasses = method.getDeclaringClass().getInterfaces();
            for (Class<?> interfaceClass : interfaceClasses) {
                Method interfaceMethod = interfaceClass.getMethod(method.getName(), method.getParameterTypes());
                if (null != interfaceMethod && interfaceMethod.isAnnotationPresent(NbClientLog.class)) {
                    return interfaceMethod;
                }
            }
            // 如果当前方法有 @NbClientLog 注解
            if (method.isAnnotationPresent(NbClientLog.class)) {
                return method;
            }
            throw new RuntimeException("Method [" + method.getName() + "] declaring class's interfaces can not find @NbClientLog");

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Can not find method [" + method.getName() + "] with @NbClientLog");
        }
    }

    /**
     * 获取方法的参数名称
     *
     * @param method 方法
     * @return 方法字段名称数组
     */
    private String[] getParamNames(Method method) {
        try {
            ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
            return parameterNameDiscoverer.getParameterNames(method);
        } catch (SecurityException e) {
            log.error("getParamNames SecurityException failed!" + method, e);
            throw new SystemException("SYSTEM_ERROR", "获取接口参数失败" + method.toGenericString());
        }
    }

    /**
     * 返回默认的返回值
     *
     * @param method     方法签名
     * @param defaultRet 返回值配置字符串， 默认为json/简单数据格式
     * @return 配置的默认返回值
     */
    private Object getDefaultReturn(Method method, String defaultRet) {
        Class<?> returnType = method.getReturnType();
        if (returnType == Void.class || defaultRet == null) {
            return null;
        }
        return JSON.parseObject(defaultRet, returnType);
    }

    /**
     * 接口配置
     */
    @Getter
    private static class MethodConfig {
        /**
         * 带注解的方法
         */
        private Method method;

        /**
         * 注解配置
         */
        private NbClientLog nbClientLog;

        /**
         * 方法签名
         */
        private String methodSign;

        /**
         * 方法参数名称
         */
        private String[] paramNames;

        /**
         * 构造方法
         *
         * @param method      方法
         * @param nbClientLog 方法注解
         * @param paramNames  方法参数名称
         */
        MethodConfig(Method method, NbClientLog nbClientLog, String[] paramNames) {
            this.method = method;
            this.nbClientLog = nbClientLog;
            this.methodSign = method.getDeclaringClass().getSimpleName() + "." + method.getName();
            this.paramNames = paramNames;
        }

    }
}
