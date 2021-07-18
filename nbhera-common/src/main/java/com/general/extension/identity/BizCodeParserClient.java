package com.general.extension.identity;

import com.general.common.exception.SystemException;
import com.general.common.util.SpringApplicationContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/6/3 0:11
 * desc: 业务身份解析器
 */
public class BizCodeParserClient {

    /**
     * 实例
     */
    private static final BizCodeParserClient INSTANCE = new BizCodeParserClient();

    /**
     * 返回单例
     *
     * @return 单例
     */
    public static BizCodeParserClient getInstance() {
        return INSTANCE;
    }

    /**
     * 获取业务code
     *
     * @param bizId 业务身份维度map
     * @return 业务code
     */
    public String getBizCode(Map<String, Object> bizId) {
        Map<String, BizCodeParser> parserMap = SpringApplicationContextHolder.getBeanByType(BizCodeParser.class);
        if (null == parserMap || parserMap.isEmpty()) {
            throw new SystemException("SYSTEM_EXTENSION", "身份解析器没有注册到容器中，身份解析器实现接口 -> com.general.extension.identity.BizCodeParser");
        }
        for (BizCodeParser parser : parserMap.values()) {
            String bizCode = parser.parser(bizId);
            if (StringUtils.isNotBlank(bizCode)) {
                return bizCode;
            }
        }
        return null;
    }

}
