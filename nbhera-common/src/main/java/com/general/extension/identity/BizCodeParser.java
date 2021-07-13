package com.general.extension.identity;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/6/3 0:11
 * desc: 解析业务身份接口
 */
public interface BizCodeParser {

    /**
     * 根据业务维度参数， 获取业务身份code
     *
     * @param bizParam 业务参数map
     * @return 业务身份code
     */
    String parser(Map<String, Object> bizParam);

    /**
     * 获取当前优先级
     *
     * @return 优先级
     */
    default int index() {
        return 0;
    }
}
