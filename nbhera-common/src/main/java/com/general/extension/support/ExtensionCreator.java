package com.general.extension.support;

/**
 * @param <ExtensionConfig> 扩展点配置
 * @author xvanning
 * date: 2021/6/1 22:52
 * desc: 本地扩展点创建者
 */
public interface ExtensionCreator<ExtensionConfig> {

    /**
     * 是否支持当前类型
     *
     * @param param 参数
     * @return 是否支持
     */
    Boolean support(Object param);

    /**
     * 注册扩展点
     *
     * @param extensionConfig 扩展点配置
     * @return 是否成功
     */
    ExtensionDefinition create(ExtensionConfig extensionConfig);

}
