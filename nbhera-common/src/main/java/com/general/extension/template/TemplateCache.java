package com.general.extension.template;

import com.general.common.exception.SystemException;
import com.general.constants.SystemCode;
import com.general.extension.enums.TemplateType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/6/1 23:09
 * desc: 模板缓存
 */
public class TemplateCache {

    /**
     * 模板缓存
     */
    private static final Map<String, BaseTemplate> TEMPLATE_CACHE = Maps.newConcurrentMap();

    /**
     * 垂直业务code
     */
    private static final List<String> BIZ_TEMPLATE = Lists.newArrayList();

    /**
     * 水平业务code
     */
    private static final List<String> HORIZONTAL_TEMPLATE = Lists.newArrayList();

    /**
     * 根据模板code返回模板
     *
     * @param templateCode 模板code
     * @return 模板
     */
    public static Template get(String templateCode) {
        return TEMPLATE_CACHE.get(templateCode);
    }

    /**
     * 返回注册到容器里的所有业务模板code
     *
     * @return 垂直业务模板code列表
     */
    public static List<String> getBizTemplateCodes() {
        return BIZ_TEMPLATE;
    }

    /**
     * 返回注册到容器里的所有水平业务模板code
     *
     * @return 水平业务模板code列表
     */
    public static List<String> getHorizontalTemplateCodes() {
        return HORIZONTAL_TEMPLATE;
    }

    /**
     * 模板注册
     *
     * @param template 模板
     * @return 模板
     */
    protected static boolean register(BaseTemplate template) {
        // 模板重复注册
        if (TEMPLATE_CACHE.containsKey(template.getTemplateCode())) {
            throw new SystemException(SystemCode.EXTENSION, "template repeat register! template code = " + template);
        }
        TEMPLATE_CACHE.put(template.getTemplateCode(), template);
        return true;
    }

    /**
     * 对内存中的模板进行检测
     */
    protected static void check() {
        if (TEMPLATE_CACHE.isEmpty()) {
            return;
        }
        TEMPLATE_CACHE.values().forEach(TemplateCache::check);
    }

    /**
     * 对模板合法性进行检测
     *
     * @param template 模板
     */
    private static void check(BaseTemplate template) {
        // 先判断是否是垂直模板
        if (template.getTemplateType() == TemplateType.VERTICAL) {
            // 校验一下订阅的模板code是否都合法
            for (String subTemplateCode : template.getSubscribe()) {
                if (Objects.equals(subTemplateCode, template.getTemplateCode())) {
                    continue;
                }
                Template subTemplate = get(subTemplateCode);
                if (null == subTemplate) {
                    throw new SystemException(SystemCode.EXTENSION, "[" + subTemplateCode + "] template code can not find");
                }
                if (subTemplate.getTemplateType() != TemplateType.HORIZONTAL) {
                    throw new SystemException(SystemCode.EXTENSION, "[" + subTemplateCode + "] is not HORIZONTAL template! it can not subscribe!");
                }
            }
            BIZ_TEMPLATE.add(template.getTemplateCode());
        } else {
            // 只有业务模板可以订阅
            if (CollectionUtils.isNotEmpty(template.getSubscribe())) {
                throw new SystemException(SystemCode.EXTENSION, "[" + template.getTemplateCode() + "] can not subscribe other template!");
            }
            HORIZONTAL_TEMPLATE.add(template.getTemplateCode());
        }
    }

}
