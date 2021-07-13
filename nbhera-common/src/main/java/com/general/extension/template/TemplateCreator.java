package com.general.extension.template;

import com.general.common.exception.SystemException;
import com.general.common.util.AssertUtils;
import com.general.common.util.BeanUtils;
import com.general.constants.SystemCode;
import com.general.extension.annotation.TemplateConfig;
import com.general.extension.enums.TemplateType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * @author xvanning
 * date: 2021/6/3 0:25
 * desc: 模板创建者
 */
@Slf4j
@AllArgsConstructor
public class TemplateCreator {

    /**
     * 模板类
     */
    private final Set<Class<? extends BaseTemplate>> templateSet;

    /**
     * 快速定义的模板类
     */
    private final Set<Field> templateFields;


    /**
     * 模板注册
     */
    public void register() {
        if (CollectionUtils.isEmpty(templateSet) && CollectionUtils.isEmpty(templateFields)) {
            return;
        }
        // 注册有单独class实现的模板类
        templateSet.stream().filter(BaseTemplate.class::isAssignableFrom).forEach(targetClass -> TemplateCache.register(BeanUtils.createBeanInstance(targetClass)));

        // 注册简单声明的模板类
        templateFields.forEach(field -> TemplateCache.register(createTemplate(field)));

        // 模板检测
        TemplateCache.check();
    }

    /**
     * 仅根据注解创建模板
     *
     * @param field 字段
     * @return 模板
     */
    private BaseTemplate createTemplate(Field field) {
        TemplateConfig config = field.getAnnotation(TemplateConfig.class);
        AssertUtils.isNotNull(config, SystemCode.EXTENSION, "@TemplateConfig is null");
        String code = StringUtils.isNotBlank(config.value()) ? config.value() : getValue(field);
        int priority = config.priority();
        String[] subscribe = config.subscribe();
        TemplateType templateType = config.type();
        if (templateType == TemplateType.VERTICAL) {
            return new BizTemplate(code, priority, subscribe);
        }
        if (templateType == TemplateType.HORIZONTAL) {
            return new DefaultHorizontalTemplate(code, priority);
        }
        throw new SystemException(SystemCode.EXTENSION, "can not support config = " + config);
    }

    /**
     * 如果字段类型是字符串，并且是static final，则尝试去获取字段值
     *
     * @param field 字段
     * @return 字段值
     */
    private String getValue(Field field) {
        if (field.getType() == String.class && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
            try {
                Object instance = BeanUtils.createBeanInstance(field.getDeclaringClass());
                field.setAccessible(true);
                return String.valueOf(field.get(instance));
            } catch (Throwable throwable) {
                log.error("get field error! ", throwable);
            }
        }
        return null;
    }


}
