package com.general.common.adapter;

import com.alibaba.fastjson.JSON;
import com.general.common.adapter.check.NotNullParamCheckProcessor;
import com.general.common.adapter.result.PageResultExceptionResultProcessor;
import com.general.common.adapter.result.ResultExceptionResultProcessor;
import com.general.common.bean.AnnotationFieldParam;
import com.general.common.bean.AnnotationMethod;
import com.general.common.bean.AnnotationParam;
import com.general.common.exception.*;
import com.general.common.util.GenericUtils;
import com.general.common.util.SpiUtils;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author xvanning
 * date: 2021/6/22 0:05
 * desc: 对外输出aop，对外接口做打点和错误处理
 */
@Slf4j
public class OutputMethodInterceptor implements MethodInterceptor {

    private static final String UNKNOWN = "UNKNOWN";

    private OutputLogger outputLogger;

    private static final ConcurrentMap<Method, AnnotationMethod> ANNOTATION_MAP_CACHE = Maps.newConcurrentMap();

    private final Map<Class<Annotation>, ParamCheckProcessor> processorMap = Maps.newConcurrentMap();

    private final Map<Class, ExceptionResultProcessor> exceptionResultProcessorMap = Maps.newConcurrentMap();

    /**
     * 默认构造函数
     */
    public OutputMethodInterceptor() {
        this(new DefaultLogger());
    }

    /**
     * 带有logger输出的构造函数
     *
     * @param outputLogger 日志输出
     */
    public OutputMethodInterceptor(OutputLogger outputLogger) {
        // 日志输出实现是否存在
        if (null == outputLogger) {
            throw new IllegalArgumentException("out put logger is required!");
        }
        this.outputLogger = outputLogger;
        // 参数校验器
        List<ParamCheckProcessor> paramCheckProcessors = Lists.newArrayList(new NotNullParamCheckProcessor());
        paramCheckProcessors.addAll(SpiUtils.load(ParamCheckProcessor.class));
        // 错误处理器
        List<ExceptionResultProcessor> exceptionResultProcessors = Lists.newArrayList(new PageResultExceptionResultProcessor(), new ResultExceptionResultProcessor());
        // spi读取
        List<ExceptionResultProcessor> spiList = SpiUtils.load(ExceptionResultProcessor.class);
        spiList.forEach(processor -> {
            try {
                GenericUtils.getGenericClass(processor.getClass());
                exceptionResultProcessors.add(processor);
            } catch (Throwable throwable) {
                log.error("获取处理器插件失败，启动时忽略 ", throwable.getMessage());
            }
        });

        // 构建cache
        // 注册错误处理器
        exceptionResultProcessors.forEach(processor -> {
            // 获取接口泛型
            Class genericClass = GenericUtils.getGenericClass(processor.getClass());
            exceptionResultProcessorMap.put(genericClass, processor);
        });

        // 如果有参数校验
        if (CollectionUtils.isNotEmpty(paramCheckProcessors)) {
            paramCheckProcessors.forEach(processor -> {
                Class genericClass = GenericUtils.getGenericClass(processor.getClass());
                processorMap.put(genericClass, processor);
            });
        }

    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 计时器
        Stopwatch stopwatch = Stopwatch.createStarted();
        // 方法签名
        Method targetMethod = methodInvocation.getMethod();
        // 类名 + 方法名
        String methodName = targetMethod.getDeclaringClass().getSimpleName() + "." + targetMethod.getName();
        // 执行1
        outputLogger.logRequest(methodName, methodInvocation.getArguments());

        Object result = null;
        boolean isSuccess = true;
        boolean throwException = false;

        try {
            result = this.checkParam(targetMethod, methodInvocation.getArguments());
            if (result == null) {
                result = methodInvocation.proceed();
            }
        }
        // 参数异常
        catch (ParamException paramException) {
            this.logException(paramException, methodName);
            result = convertFailedResult(paramException, targetMethod);
        }
        // 业务异常
        catch (BusinessException businessException) {
            logException(businessException, methodName);
            result = convertFailedResult(businessException, targetMethod);
        }
        // 三方系统调用异常或者系统调用异常
        catch (ThirdException | SystemException ex) {
            this.logError(methodName, ex);
            logException(ex, methodName);
            // 对外隐藏真实错误
            result = convertFailedResult(ex.getCode(), UNKNOWN, targetMethod.getReturnType(), ex);
            isSuccess = false;
        }
        // 未知异常
        catch (Throwable throwable) {
            outputLogger.logThrowable(methodName, throwable);
            // 转换为系统异常
            result = convertFailedResult(UNKNOWN, UNKNOWN, targetMethod.getReturnType(), throwable);
            isSuccess = false;

            // 是否带有@ThrowException注解
            if (AnnotationUtils.findAnnotation(targetMethod, ThrowException.class) != null) {
                throwException = true;
                throw throwable;
            }
        } finally {
            outputLogger.logResponse(methodName, result, isSuccess, throwException, stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
        }
        return result;
    }

    /**
     * 检查参数是否通过，对不通过的构建错误返回值
     *
     * @param targetMethod 方法
     * @param args         入参
     * @return 如果返回不为null，说明没有通过校验
     */
    private Object checkParam(Method targetMethod, Object[] args) {
        // 如果有参数校验器，则开始进行参数校验
        if (processorMap.isEmpty()) {
            return null;
        }
        // 获取索引缓存对象
        AnnotationMethod annotationMethod = ANNOTATION_MAP_CACHE.computeIfAbsent(targetMethod, value -> AnnotationMethod.of(this.getInterfaceMethod(targetMethod), Lists.newArrayList(processorMap.keySet())));

        // 没有注解快速返回
        if (null == annotationMethod || annotationMethod.isEmpty()) {
            return null;
        }

        // 遍历注释
        for (Map.Entry<Class<? extends Annotation>, List<AnnotationParam>> entry : annotationMethod.getAnnotationMap().entrySet()) {
            Class<? extends Annotation> annotationClass = entry.getKey();
            List<AnnotationParam> params = entry.getValue();
            ParamCheckProcessor processor = processorMap.get(annotationClass);
            if (null == processor) {
                continue;
            }
            // 遍历参数
            for (AnnotationParam annotationParam : params) {
                Object paraObject = args[annotationParam.getIndex()];
                // 如果不需要下探
                if (!annotationParam.isDeep()) {
                    Annotation annotation = annotationParam.getAnnotation();
                    // 进行校验
                    if (!processor.check(annotation, annotationParam.getType(), paraObject)) {
                        return convertFailedResult("PARAM_ERROR", processor.getMessage(annotation, paraObject), targetMethod.getReturnType(), null);
                    }
                }
                // 如果需要下探
                else {
                    for (AnnotationFieldParam fieldParam : annotationParam.getFields()) {
                        Object fieldValue = fieldParam.getValue(paraObject);
                        Annotation annotation = fieldParam.getAnnotation();
                        if (!processor.check(annotation, annotationParam.getType(), fieldValue)) {
                            return convertFailedResult("PARAM_ERROR", processor.getMessage(annotation, paraObject), targetMethod.getReturnType(), null);
                        }
                    }
                }
            }

        }

        return null;
    }


    /**
     * 获取方法的接口对应的方法
     *
     * @param method 方法
     * @return 接口方法
     */
    private Method getInterfaceMethod(Method method) {
        try {
            // 优先从接口中获取
            Class<?>[] interfaceClasses = method.getDeclaringClass().getInterfaces();
            for (Class<?> interfaceClass : interfaceClasses) {
                Method[] methods = interfaceClass.getMethods();
                for (Method interfaceMethod : methods) {
                    if (Objects.equals(interfaceMethod.getName(), method.getName()) &&
                            interfaceMethod.getParameterCount() == method.getParameterCount()) {
                        return interfaceClass.getMethod(method.getName(), method.getParameterTypes());
                    }
                }

            }
        } catch (Exception e) {
            // skip
        }
        return method;
    }

    /**
     * 将异常信息转为正常结果返回值进行返回
     *
     * @param withCodeException 带异常码的异常
     * @param method            方法签名
     * @return 返回结果值
     */
    private Object convertFailedResult(WithCodeException withCodeException, Method method) {
        return convertFailedResult(withCodeException.getCode(), withCodeException.getMessage(), method.getReturnType(), withCodeException);
    }

    /**
     * 将异常信息转为正常结果返回值进行返回
     *
     * @param code       异常码
     * @param message    异常信息
     * @param returnType 接口返回值
     * @param throwable  异常
     * @return 返回结果值
     */
    private Object convertFailedResult(String code, String message, Class<?> returnType, Throwable throwable) {
        ExceptionResultProcessor processor = exceptionResultProcessorMap.get(returnType);
        if (null != processor) {
            return processor.convert(code, message, throwable);
        }
        log.error("通用异常处理无法支持当前方法 = {}", throwable);
        return null;
    }

    /**
     * 异常码通用记录方法
     *
     * @param exception  异常信息
     * @param methodName 方法名称
     */
    private void logException(WithCodeException exception, String methodName) {
        StackTraceElement trace = this.getTrace(exception.getStackTrace());
        String location = trace != null ? trace.getClassName() + "." + trace.getMethodName() + "." + trace.getLineNumber() : "";
        log.error("异常码统计[{}][{}][{}][{}]", exception.getClass().getSimpleName(), methodName, exception.getCode(), location);
    }

    /**
     * 针对部分情况需要输出异常信息
     *
     * @param methodName 方法签名
     * @param exception  异常
     */
    private void logError(String methodName, WithCodeException exception) {
        outputLogger.logSystemError(methodName, exception.getExtension(), exception);
    }

    /**
     * 异常栈中寻找第一个非断言工具类的信息
     *
     * @param stack 异常栈
     * @return 产生异常的未知信息
     */
    private StackTraceElement getTrace(StackTraceElement[] stack) {
        if (null == stack || stack.length == 0) {
            return null;
        }
        // 查找第一个非断言工具类的异常栈信息
        for (StackTraceElement trace : stack) {
            if ("com.general.common.util.AssertUtils".equals(trace.getClassName())) {
                return trace;
            }
        }
        return null;
    }

    /**
     * 默认日志输出
     */
    private static class DefaultLogger implements OutputLogger {

        @Override
        public void logRequest(String methodName, Object[] args) {
            log.info("|RPC-REQ [{}]| request = {}", methodName, JSON.toJSONString(args));
        }

        @Override
        public void logSystemError(String methodName, Map<String, Object> extension, WithCodeException withCodeException) {
            log.error("详细异常信息:{}", extension, withCodeException);
        }

        @Override
        public void logThrowable(String methodName, Throwable throwable) {
            log.error("服务发生异常，[service = {}]", methodName, throwable);
        }

        @Override
        public void logResponse(String methodName, Object response, boolean success, boolean throwException, Long costTime) {
            log.info("|RPC-RESP [{}][{}][{}ms]| result = {}", methodName, success, costTime, throwException ? "throw exception" : JSON.toJSONString(response));
        }
    }
}
