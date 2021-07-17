package com.general.extension.biz;

import com.alibaba.fastjson.util.TypeUtils;
import com.general.extension.identity.BizCodeParser;
import com.general.extension.template.TemplateDemo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/7/17 17:09
 * desc:
 */
@Component
public class VerticalBizParser implements BizCodeParser {
    @Override
    public String parser(Map<String, Object> bizParam) {
        Integer businessType = TypeUtils.castToInt(bizParam.get("businessType"));
        if (Objects.equals(businessType, 1)) {
            return TemplateDemo.SCHOOL_BIZ;
        } else if (Objects.equals(businessType, 2)) {
            return TemplateDemo.COMMUNITY_BIZ;
        }
        return TemplateDemo.COMMUNITY_BIZ;
    }
}
