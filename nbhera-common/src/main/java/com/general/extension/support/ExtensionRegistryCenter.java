package com.general.extension.support;

import com.alibaba.fastjson.JSON;
import com.general.common.exception.SystemException;
import com.general.common.util.AssertUtils;
import com.general.common.util.SpiUtils;
import com.general.constants.SystemCode;
import com.general.extension.template.Template;
import com.general.extension.template.TemplateCache;
import com.general.nbhera.extension.ExtensionPoints;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xvanning
 * date: 2021/6/1 22:45
 * desc: 扩展点管理器
 */
@Slf4j
public class ExtensionRegistryCenter {

    /**
     * 扩展点默认实现
     */
    private static final Map<Class<?>, ExtensionDefinition> DEFAULT_CACHE = Maps.newConcurrentMap();

    /**
     * 扩展点注册 Map
     */
    private static final Map<Class<? extends ExtensionPoints>, Map<String, ExtensionCache>> REGISTER_CACHE = Maps.newConcurrentMap();

    /**
     * 扩展点创建者
     */
    private static final List<ExtensionCreator> CREATORS = SpiUtils.load(ExtensionCreator.class);


    /**
     * 注册扩展点
     *
     * @param extensionConfig 扩展点定义
     * @return 是否成功
     */
    protected static Boolean register(Object extensionConfig) {
        return register(createExtensionDefinition(extensionConfig));
    }

    /**
     * 根据传入的扩展点注解的value值和扩展接口类，获得相应的实现子类实例
     *
     * @param templateCode            扩展点注解的value值
     * @param scenario                场景code
     * @param extensionInterfaceClass 扩展接口类
     * @param <E>                     泛型，表示具体的接口
     * @param <R>                     泛型，表示决策对象泛型
     * @return 具体的实现子类
     */
    protected static <E extends ExtensionPoints<E, R>, R> E getExtensionPoint(String templateCode, String scenario, Class<E> extensionInterfaceClass) {
        return getExtensionPoint(templateCode, scenario, extensionInterfaceClass, true);
    }

    /**
     * 获取接口的默认实现扩展点定义
     *
     * @param extensionInterfaceClass 接口类
     * @param <E>                     泛型，表示接口类
     * @param <R>                     泛型，表示决策对象
     * @return 默认实现的扩展点定义
     */
    protected static <E extends ExtensionPoints<E, R>, R> ExtensionDefinition getDefaultDefinition(Class<E> extensionInterfaceClass) {
        return DEFAULT_CACHE.get(extensionInterfaceClass);
    }

    /**
     * 注册扩展点
     *
     * @param definition 扩展点定义
     * @return 是否成功
     */
    protected static Boolean register(ExtensionDefinition definition) {
        AssertUtils.isNotNull(definition, SystemCode.EXTENSION, "注册扩展点定义不能为空");
        AssertUtils.isNotNull(definition.getCode(), SystemCode.EXTENSION, "注册扩展点code不能为空");
        AssertUtils.isNotNull(definition.getExtensionPoint(), SystemCode.EXTENSION, "注册扩展点实现不能为空");
        Class<?> targetClass = definition.getTargetClass();

        // 如果是默认实现
        if (definition.getIsDefault()) {
            ExtensionDefinition defaultDefinition = DEFAULT_CACHE.put(targetClass, definition);
            if (null != defaultDefinition && defaultDefinition.equals(definition)) {
                throw new SystemException(SystemCode.EXTENSION, "extension default implements already exists class = " + targetClass + " find class = " + Lists.newArrayList(defaultDefinition.getExtensionPointsClass(), defaultDefinition.getExtensionPointsClass()));
            }
            return true;
        }

        // 如果不是默认实现，key - 模板code
        Map<String, ExtensionCache> cacheMap = REGISTER_CACHE.computeIfAbsent(definition.getTargetClass(), value -> Maps.newConcurrentMap());
        ExtensionCache extensionCache = cacheMap.computeIfAbsent(definition.getCode(), value -> ExtensionCache.of(definition.getTargetClass(), definition.getCode()));
        log.info("|EXT_REGISTER| register {}", definition);

        return extensionCache.put(definition);
    }

    /**
     * 根据传入的扩展点注解的value值和扩展接口类，获得相应的实现子类实例
     *
     * @param templateCode            扩展点注解的value值
     * @param scenario                场景code
     * @param extensionInterfaceClass 扩展接口类
     * @param check                   如果为true，在获取不到实现的时候，抛出异常
     * @param <E>                     泛型，表示具体的接口
     * @param <R>                     泛型，表示决策对象泛型
     * @return 具体的实现子类
     */
    protected static <E extends ExtensionPoints<E, R>, R> E getExtensionPoint(String templateCode, String scenario, Class<E> extensionInterfaceClass, Boolean check) {
        ExtensionDefinition<E, R> definition = getExtensionDefinition(templateCode, scenario, extensionInterfaceClass, true);
        E extensionPoint = null == definition ? null : definition.getExtensionPoint();
        if (check && null == extensionPoint) {
            String message = "can not find extension point, targetClass = " + extensionInterfaceClass + ", code = " + templateCode + ", scenario = " + scenario;
            throw new SystemException(SystemCode.EXTENSION, message);
        }
        return extensionPoint;
    }

    /**
     * 根据传入的扩展点注解的value值和扩展接口类，获得相应的 ExtensionDefinition
     *
     * @param templateCode            扩展点注解的value值
     * @param scenario                场景code
     * @param extensionInterfaceClass 扩展接口类
     * @param returnDefault           是否返回默认实现
     * @param <E>                     泛型，表示具体的接口
     * @param <R>                     决策对象泛型
     * @return 具体的实现子类
     */
    protected static <E extends ExtensionPoints<E, R>, R> ExtensionDefinition<E, R> getExtensionDefinition(String templateCode, String scenario, Class<E> extensionInterfaceClass, boolean returnDefault) {

        // 获取第一层map
        Map<String, ExtensionCache> extensionClassMap = REGISTER_CACHE.get(extensionInterfaceClass);
        if (null == extensionClassMap) {
            return null;
        }

        // 获取第二层map
        ExtensionDefinition definition = null;
        ExtensionCache extensionCache = extensionClassMap.get(templateCode);
        if (extensionCache != null) {
            definition = extensionCache.getExtensionPoint(scenario);
        }

        // 如果扩展点为空，则返回默认实现
        if (null == definition && returnDefault) {
            definition = getDefaultDefinition(extensionInterfaceClass);
        }

        return definition;
    }

    /**
     * 策略模式获取扩展点
     *
     * @param reduceTarget 策略对象
     * @param targetClass  扩展点接口类
     * @param <E>          泛型，表示具体的接口
     * @param <R>          决策对象泛型
     * @return 扩展点实现
     */
    protected static <E extends ExtensionPoints<E, R>, R> List<ExtensionDefinition<E, R>> getStrategy(R reduceTarget, Class<E> targetClass) {
        return getStrategy(null, reduceTarget, targetClass);
    }

    /**
     * 策略模式获取扩展点
     *
     * @param scenario     场景code
     * @param reduceTarget 策略对象
     * @param targetClass  扩展点接口类
     * @param <R>          泛型，表示具体的接口
     * @param <E>          决策对象泛型
     * @return 扩展点实现
     */
    protected static <E extends ExtensionPoints<E, R>, R> List<ExtensionDefinition<E, R>> getStrategy(String scenario, R reduceTarget, Class<E> targetClass) {
        Map<String, ExtensionCache> extensionClassMap = REGISTER_CACHE.get(targetClass);
        if (MapUtils.isNotEmpty(extensionClassMap)) {
            return extensionClassMap.values().stream().filter(ExtensionCache::getIsStrategy).map(cache -> {
                ExtensionDefinition<E, R> definition = cache.getExtensionPoint(scenario);
                return null != definition && definition.support(reduceTarget, null) ? definition : null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 根据多个模板code 返回多个符合条件的扩展点实现定义
     *
     * @param templateCodes 扩展点模板code列表
     * @param scenario      场景code
     * @param reduceTarget  决策对象
     * @param targetClass   扩展点class
     * @param <E>           泛型，表示具体的接口
     * @param <R>           决策对象泛型
     * @return 扩展点实现定义列表
     */
    protected static <E extends ExtensionPoints<E, R>, R> List<ExtensionDefinition<E, R>> getExtensionDefinitionList(List<String> templateCodes, String scenario, R reduceTarget, Class<E> targetClass) {
        if (CollectionUtils.isEmpty(templateCodes)) {
            return Collections.emptyList();
        }
        return getExtensionDefinitionList(null, templateCodes, scenario, reduceTarget, targetClass);
    }

    /**
     * 根据 bizCode 返回多个符合条件的扩展点实现定义
     *
     * @param bizCode      业务code
     * @param scenario     场景code
     * @param reduceTarget 决策对象
     * @param targetClass  扩展点class
     * @param <E>          泛型，表示具体的接口
     * @param <R>          决策对象泛型
     * @return 扩展点实现定义列表
     */
    protected static <E extends ExtensionPoints<E, R>, R> List<ExtensionDefinition<E, R>> getExtensionDefinitionList(String bizCode, String scenario, R reduceTarget, Class<E> targetClass) {
        Template template = TemplateCache.get(bizCode);
        if (CollectionUtils.isEmpty(template.getSubscribe())) {
            return Collections.emptyList();
        }
        return getExtensionDefinitionList(bizCode, template.getSubscribe(), scenario, reduceTarget, targetClass);
    }

    /**
     * 获取具体子类中实现了哪一个扩展点接口
     *
     * @param extensionPointClass 具体实现子类
     * @return 扩展点接口类
     */
    protected static Class<? extends ExtensionPoints> getExtensionInterfaceClass(Class<?> extensionPointClass) {
        if (Objects.isNull(extensionPointClass) || Objects.equals(extensionPointClass, Object.class)) {
            return null;
        }
        Class<?>[] interfaces = extensionPointClass.getInterfaces();
        Class<?> curClass = null;
        if (interfaces.length != 0) {
            curClass = Arrays.stream(interfaces)
                    .filter(ExtensionPoints.class::isAssignableFrom)
                    .findFirst().orElse(null);
        }
        return Objects.nonNull(curClass) ? (Class<? extends ExtensionPoints>) curClass : getExtensionInterfaceClass(extensionPointClass.getSuperclass());
    }


    /**
     * 根据多个模板code 返回多个符合条件的扩展点实现定义
     *
     * @param bizCode       业务code
     * @param templateCodes 扩展点模板code列表
     * @param scenario      场景code
     * @param reduceTarget  决策对象
     * @param targetClass   扩展点class
     * @param <E>           泛型，表示具体的接口
     * @param <R>           决策对象泛型
     * @return 扩展点实现定义列表
     */
    protected static <E extends ExtensionPoints<E, R>, R> List<ExtensionDefinition<E, R>> getExtensionDefinitionList(String bizCode, List<String> templateCodes, String scenario, R reduceTarget, Class<E> targetClass) {
        List<ExtensionDefinition<E, R>> result = templateCodes.stream().map(templateCode -> getExtensionDefinition(templateCode, scenario, targetClass, false)).filter(Objects::nonNull).filter(definition -> definition.support(reduceTarget, bizCode)).sorted().distinct().collect(Collectors.toList());

        // 如果没有匹配到实现，就获取默认实现
        if (CollectionUtils.isEmpty(result)) {
            ExtensionDefinition<E, R> definition = DEFAULT_CACHE.get(targetClass);
            if (null != definition) {
                result = Lists.newArrayList(definition);
            }
        }
        return result;
    }

    /**
     * 根据扩展点配置生成扩展点定义类
     *
     * @param extensionConfig 扩展点配置
     * @param <E>             泛型，表示具体的接口
     * @param <R>             决策对象泛型
     * @return 扩展点定义类
     */
    private static <E extends ExtensionPoints<E, R>, R> ExtensionDefinition<E, R> createExtensionDefinition(Object extensionConfig) {
        for (ExtensionCreator creator : CREATORS) {
            if (creator.support(extensionConfig)) {
                ExtensionDefinition<E, R> definition = creator.create(extensionConfig);
                definition.setTemplate(TemplateCache.get(definition.getCode()));
                return definition;
            }
        }
        throw new SystemException(SystemCode.EXTENSION, JSON.toJSONString(extensionConfig));
    }
}
