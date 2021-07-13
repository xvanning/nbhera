package com.general.extension.boot;

import com.alibaba.fastjson.util.TypeUtils;
import com.general.common.bean.AnnotationFieldParam;
import com.general.common.bean.AnnotationMethod;
import com.general.common.bean.AnnotationParam;
import com.general.common.exception.SystemException;
import com.general.constants.SystemCode;
import com.general.extension.annotation.BizCode;
import com.general.extension.annotation.ExtensionSession;
import com.general.extension.identity.BizCodeParserClient;
import com.general.extension.session.BizSessionScope;
import com.general.nbhera.extension.aonnotation.BizParam;
import com.general.nbhera.extension.aonnotation.Scenario;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xvanning
 * date: 2021/5/30 21:32
 * desc: @ExtensnionSession 注解的切面实现类
 */
@Slf4j
public class SessionServiceMethodInterceptor implements MethodInterceptor {

    protected static final List<Class<? extends Annotation>> CHECK_ANNOTATION = ImmutableList.of(BizParam.class, BizCode.class, Scenario.class);

    /**
     * 注解缓存
     */
    protected static final ConcurrentMap<Method, AnnotationMethod> ANNOTATION_MAP_CACHE = Maps.newConcurrentMap();

    /**
     * 接口方法缓存
     */
    private static final ConcurrentMap<Method, Method> SESSION_INTERFACE_METHOD_CACHE = Maps.newConcurrentMap();


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 获取方法声明class的@ExtensionSession接口对应的方法
        Method sessionMethod = getSessionInterfaceMethod(invocation.getMethod());
        // 获取索引缓存对象
        AnnotationMethod annotationMethod = ANNOTATION_MAP_CACHE.computeIfAbsent(sessionMethod, value -> AnnotationMethod.of(sessionMethod, CHECK_ANNOTATION));
        // 获取身份维度信息
        Map<String, Object> bizParamMap = null;
        // 获取业务身份code
        String bizCode = null;

        // 如果有@bizCode注解则优先获取
        if (annotationMethod.contains(BizCode.class)) {
            bizCode = resolveBizCode(annotationMethod, invocation.getArguments());
        }

        // 如果没有获取，则尝试从参数获取
        if (StringUtils.isBlank(bizCode)) {
            bizParamMap = resolveVizParamMap(annotationMethod, invocation.getArguments());
            bizCode = BizCodeParserClient.getInstance().getBizCode(bizParamMap);
        }

        // 识别场景
        String scenario = resolveScenario(annotationMethod, invocation.getArguments());
        // 如果无法获取业务身份
        if (StringUtils.isBlank(bizCode)) {
            throw new SystemException(SystemCode.EXTENSION, "can not parser bizCode" + Arrays.toString(invocation.getArguments()));
        }

        // 创建一次调用session
        try {
            return new BizSessionScope<Object>(bizCode, scenario, bizParamMap) {
                @Override
                protected Object execute() {
                    try {
                        return invocation.proceed();
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                }
            }.invoke();
        } finally {
            // skip, maybe have something
        }

    }

    /**
     * 根据方法参数获取场景
     *
     * @param annotationMethod 当前方法
     * @param arguments        方法的参数
     * @return 场景
     */
    private String resolveScenario(AnnotationMethod annotationMethod, Object[] arguments) {
        return getValue(annotationMethod, arguments, Scenario.class);
    }

    /**
     * 根据方法参数获取业务身份维度参数map
     *
     * @param annotationMethod 当前党法
     * @param args             方法的参数
     * @return key - 维度key， value - 维度value
     */
    private Map<String, Object> resolveVizParamMap(AnnotationMethod annotationMethod, Object[] args) {
        // 构建返回值
        Map<String, Object> result = Maps.newHashMap();
        if (null == annotationMethod || annotationMethod.isEmpty() || !annotationMethod.contains(BizParam.class)) {
            return result;
        }

        // 如果包含注解，则开始解析
        List<AnnotationParam> annotationParams = annotationMethod.get(BizParam.class);
        annotationParams.stream().filter(annotationParam -> null != args[annotationParam.getIndex()]).forEach(annotationParam -> {
            Object param = args[annotationParam.getIndex()];
            if (null == param) {
                return;
            }

            if (!annotationParam.isDeep()) {
                String key = annotationParam.getAnnotation(BizParam.class).value();
                if (StringUtils.isBlank(key)) {
                    throw new SystemException("SYSTEM_EXTENSION", "字段需要指定value");
                }
                result.put(key, param);
                return;
            }
            annotationParam.getFields().forEach(field -> {
                String key = field.getAnnotation(BizParam.class).value();
                if (StringUtils.isBlank(key)) {
                    key = field.getName();
                }
                result.put(key, field.getValue(param));
            });
        });
        return result;
    }

    /**
     * 根据方法参数获取业务code
     *
     * @param annotationMethod 当前方法
     * @param arguments        方法的参数
     * @return 业务code
     */
    private String resolveBizCode(AnnotationMethod annotationMethod, Object[] arguments) {
        return getValue(annotationMethod, arguments, BizCode.class);
    }

    /**
     * 根据当前参数，获取第一个指定注解的值
     *
     * @param annotationMethod 方法
     * @param arguments        参数
     * @param annotation       注解
     * @return 值
     */
    private String getValue(AnnotationMethod annotationMethod, Object[] arguments, Class<? extends Annotation> annotation) {
        if (null == annotationMethod || annotationMethod.isEmpty() || !annotationMethod.contains(annotation)) {
            return null;
        }

        // 如果包含注解，则开始解析，只会有一个
        AnnotationParam annotationParam = annotationMethod.get(annotation).stream().findFirst().orElse(null);
        if (null == annotationParam) {
            return null;
        }

        Object value = arguments[annotationParam.getIndex()];
        if (value == null) {
            return null;
        }

        if (annotationParam.isDeep()) {
            AnnotationFieldParam annotationFieldParam = annotationParam.getFields().stream().findFirst().orElse(null);
            if (null != annotationFieldParam && String.class == annotationFieldParam.getType()) {
                return TypeUtils.castToString(annotationFieldParam.getValue(value));
            }
        } else {
            return TypeUtils.castToString(value);
        }
        return null;
    }

    /**
     * 获取带 @ExtensionSession 注解的真实方法
     *
     * @param method 方法
     * @return 真是方法
     */
    private Method getSessionInterfaceMethod(Method method) {
        return SESSION_INTERFACE_METHOD_CACHE.computeIfAbsent(method, value -> {
            try {
                // 优先从接口中获取
                Class<?>[] interfacesClasses = method.getDeclaringClass().getInterfaces();
                for (int i = 0; i < interfacesClasses.length; i++) {
                    Class<?> interfaceClass = interfacesClasses[i];
                    Method interfaceMethod = interfaceClass.getMethod(method.getName(), method.getParameterTypes());
                    if (null != interfaceMethod && interfaceMethod.isAnnotationPresent(ExtensionSession.class)) {
                        return interfaceMethod;
                    }
                }
                // 当前方法有 session 注解
                if (method.isAnnotationPresent(ExtensionSession.class)) {
                    return method;
                }
            } catch (NoSuchMethodException e) {
                // skip
            }
            throw new RuntimeException("Method [" + method.getName() + "] declaring class's interfaces can not find @ExtensionSession interface");
        });

    }
}
