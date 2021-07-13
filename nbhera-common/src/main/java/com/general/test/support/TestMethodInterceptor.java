package com.general.test.support;

import com.alibaba.fastjson.JSON;
import com.general.common.exception.SystemException;
import com.general.common.util.SpiUtils;
import com.general.test.ExpressionTestContextAdapter;
import com.general.test.SessionContext;
import com.general.test.annotation.NbTest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xvanning
 * date: 2021/7/1 0:55
 * desc: 测试接口aop处理
 */
@Slf4j
public class TestMethodInterceptor implements MethodInterceptor {

    /**
     * key - 代理method， value - 带有@Nbtest注解的真实方法
     */
    private static final ConcurrentMap<Method, MethodConfig> INTERFACE_METHOD_CACHE = Maps.newConcurrentMap();

    /**
     * 处理器
     */
    private static final Map<String, ExpressionTestContextAdapter> ADAPTER_CACHE = Maps.newConcurrentMap();


    /**
     * 构造方法
     */
    public TestMethodInterceptor() {
        // 默认实现方法
        List<ExpressionTestContextAdapter> adapters = Lists.newArrayList(new DefaultExpressionTestContextAdapter());
        // 读取spi
        adapters.addAll(SpiUtils.load(ExpressionTestContextAdapter.class));
        // 设置到缓存
        adapters.forEach(adapter -> ADAPTER_CACHE.put(adapter.getNameSpace(), adapter));
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 获取方法声明 class 的 @NbClientLog 接口对应的方法声明
        MethodConfig methodConfig = getAnnotationMethodConfig(invocation);
        if (SessionContext.isTest()) {
            ExpressionTestContextAdapter adapter = ADAPTER_CACHE.get(methodConfig.getNbTest().namespace());
            if (null != adapter) {
                // 获取参数
                Map<String, Object> env = this.getEnv(invocation.getArguments(), methodConfig);
                if (!adapter.isMatch(env, methodConfig.getNbTest().expression())) {
                    // 命中规则输出日志
                    log.info("test invoker method = [{}], param = [{}]", methodConfig.getMethod().getName(), JSON.toJSONString(invocation.getArguments()));
                    return adapter.defaultResult(invocation.getMethod().getReturnType(), methodConfig.nbTest, invocation.getArguments());
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 根据参数名称组装环境变量
     *
     * @param args         参数
     * @param methodConfig 配置
     * @return 动态计算使用环境变量
     */
    private Map<String, Object> getEnv(Object[] args, MethodConfig methodConfig) {
        Map<String, Object> env = Maps.newHashMap();
        // 如果没有参数，则直接返回空 map
        if (null == args || args.length == 0) {
            return env;
        }
        for (int index = 0; index < args.length; index++) {
            env.put(methodConfig.getParamNames()[index], args[index]);
        }
        return env;
    }

    /**
     * 获取方法声明class的@NbclientLog 接口对应的方法相关配置参数
     *
     * @param invocation 代理对象
     * @return 带注解的方法
     */
    private MethodConfig getAnnotationMethodConfig(MethodInvocation invocation) {
        Method targetMethod = invocation.getMethod();
        return INTERFACE_METHOD_CACHE.computeIfAbsent(targetMethod, value -> {
            Method method = this.getAnnotationMethod(targetMethod);
            NbTest nbTest = method.getAnnotation(NbTest.class);
            String[] paramNames = this.getParamNames(method);
            return new MethodConfig(method, nbTest, paramNames);
        });

    }

    /**
     * 根据接口获取返回的真实方法
     *
     * @param method 方法
     * @return 带注解的真实方法
     */
    private Method getAnnotationMethod(Method method) {
        try {
            // 优先从接口中获取
            Class[] interfaceClasses = method.getDeclaringClass().getInterfaces();
            for (int i = 0; i < interfaceClasses.length; i++) {
                Class<?> interfaceClass = interfaceClasses[i];
                Method interfaceMethod = interfaceClass.getMethod(method.getName(), method.getParameterTypes());
                if (null != interfaceMethod && interfaceMethod.isAnnotationPresent(NbTest.class)) {
                    return interfaceMethod;
                }
            }

            // 如果当前方法有 @NbClientLog 注解
            if (method.isAnnotationPresent(NbTest.class)) {
                return method;
            }
            throw new RuntimeException("Method[" + method.getName() + "] declaring class's interfaces can not find @NbClientLog interface");

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Can not find method [" + method.getName() + "] from @NbClientLog interface");
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
        private NbTest nbTest;

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
         * @param method     方法
         * @param nbTest     方法注解
         * @param paramNames 方法参数名称
         */
        MethodConfig(Method method, NbTest nbTest, String[] paramNames) {
            this.method = method;
            this.nbTest = nbTest;
            this.methodSign = method.getDeclaringClass().getSimpleName() + "." + method.getName();
            this.paramNames = paramNames;
        }

    }
}
