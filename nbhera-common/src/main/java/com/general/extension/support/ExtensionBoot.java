package com.general.extension.support;

import com.general.common.util.AssertUtils;
import com.general.common.util.SpringApplicationContextHolder;
import com.general.constants.SystemCode;
import com.general.extension.identity.BizCodeParserClient;
import com.general.extension.template.TemplateCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xvanning
 * date: 2021/6/3 0:05
 * desc: 扩展点启动器
 */
@Slf4j
@Getter
@AllArgsConstructor
public class ExtensionBoot {

    /**
     * 扫描路径
     */
    private String[] scanPackage;

    /**
     * 扩展点扫描类
     */
    private ExtensionClassSet extensionClassSet;

    /**
     * 扩展点启动类
     *
     * @param scanPackage 扫描包路径
     */
    public ExtensionBoot(String[] scanPackage) {
        this(scanPackage, ExtensionClassSet.of(scanPackage));
    }

    /**
     * 初始化方法
     */
    public void init() {
        // 目前强依赖spring上下文
        AssertUtils.isNotNull(SpringApplicationContextHolder.getApplicationContext(), SystemCode.EXTENSION, "需要在上下文中注册SpringApplicationContextHolder");

        // 检查是否存在身份解析器
        BizCodeParserClient.getInstance();

        // 注册模板
        log.info("|EXT_BOOT| register template start!");
        new TemplateCreator(extensionClassSet.getTemplateClasses(), extensionClassSet.getTemplateFields()).register();

        // 注册扩展点
        log.info("|EXT_BOOT| register extension point start!");
        this.extensionClassSet.getExtensionClasses().forEach(ExtensionRegistryCenter::register);
    }
}
