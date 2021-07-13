package com.general.extension.support;

import com.general.extension.annotation.Extension;
import com.general.extension.annotation.TemplateConfig;
import com.general.extension.template.BaseTemplate;
import com.general.nbhera.extension.ExtensionPoints;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xvanning
 * date: 2021/5/30 21:42
 * desc: 就扩展点需要解析类集合
 */
@Getter
@NoArgsConstructor
public class ExtensionClassSet {

    private static final String SCAN_PACKAGE = "com.nbhera";

    /**
     * 模板类
     */
    private Set<Class<? extends BaseTemplate>> templateClasses;

    /**
     * 快速定义的模板类
     */
    private Set<Field> templateFields;

    /**
     * 扩展点定义类
     */
    private Set<Class<? extends ExtensionPoints>> extensionPointClasses;

    /**
     * 扩展点实现类
     */
    private Set<Class<? extends ExtensionPoints>> extensionClasses;

    /**
     * 创建
     *
     * @param scanPackage 扫描路径
     * @return 扩展点需要扫描类
     */
    public static ExtensionClassSet of(String[] scanPackage) {
        if (scanPackage == null || scanPackage.length == 0) {
            scanPackage = new String[]{SCAN_PACKAGE};
        }

        ExtensionClassSet set = new ExtensionClassSet();

        // 扫包，指定路径URL
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(scanPackage)
                .addScanners(new SubTypesScanner()).addScanners(new FieldAnnotationsScanner()));

        // 扫描所有模板
        Set<Class<?>> templates = reflections.getTypesAnnotatedWith(TemplateConfig.class);
        set.templateClasses = templates.stream()
                .filter(BaseTemplate.class::isAssignableFrom)
                .map(targetClass -> (Class<? extends BaseTemplate>) targetClass).collect(Collectors.toSet());

        // 扫描带模板的字段注解，这种一般为不需要重写support的模板类，支持快速配置
        set.templateFields = reflections.getFieldsAnnotatedWith(TemplateConfig.class);

        // 获取所有@Extension注解的类
        Set<Class<?>> extensionSet = reflections.getTypesAnnotatedWith(Extension.class);
        set.extensionClasses = extensionSet.stream().map(ExtensionClassSet::convertExtensionImplClass)
                .filter(Objects::nonNull).collect(Collectors.toSet());

        // 获取所有扩展点定义类
        Set<Class<? extends ExtensionPoints>> extentionPoints = reflections.getSubTypesOf(ExtensionPoints.class);
        set.extensionPointClasses = extentionPoints.stream().filter(targetClass -> targetClass != ExtensionPoints.class)
                .filter(targetClass -> targetClass.isInterface()).collect(Collectors.toSet());

        // 如果扩展点实现存在，但是没有扫描到扩展点类，则从实现类解析接口类
        if (CollectionUtils.isNotEmpty(set.extensionClasses) && CollectionUtils.isEmpty(set.extensionPointClasses)) {
            set.extensionPointClasses = set.extensionClasses.stream().map(targetClass -> (Class<ExtensionPoints>) ExtensionRegistryCenter.getExtensionInterfaceClass(targetClass)).filter(targetClass -> targetClass.isInterface()).collect(Collectors.toSet());
        }
        return set;
    }

    /**
     * 转化过滤扩展点实现类，需要当前类不是接口或者抽象类
     *
     * @param targetClass 目标类
     * @return 扩展点实现类
     */
    private static Class<? extends ExtensionPoints> convertExtensionImplClass(Class<?> targetClass) {
        if (null == targetClass) {
            return null;
        }
        int modifiers = targetClass.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)
                || !ExtensionPoints.class.isAssignableFrom(targetClass)) {
            return null;
        }
        return (Class<? extends ExtensionPoints>) targetClass;
    }

    @Override
    public String toString() {
        return "load ExtensionClassSet success {" +
                "templateClasses=" + templateClasses +
                ", templateFields=" + templateFields +
                ", extensionPointClasses=" + extensionPointClasses +
                ", extensionClasses=" + extensionClasses +
                '}';
    }
}
