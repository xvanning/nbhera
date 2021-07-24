package com.general.extension.config;

import com.alibaba.fastjson.util.TypeUtils;
import com.general.enums.BusinessTypeEnums;
import com.general.extension.identity.BizCodeParser;
import com.general.extension.template.TemplateDemo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author xvanning
 * date: 2021/7/17 17:09
 * desc: 业务标识解析器
 */
@Component
public class VerticalBizParser implements BizCodeParser {

    @Override
    public String parser(Map<String, Object> bizParam) {
        Integer businessType = TypeUtils.castToInt(bizParam.get("businessType"));
        if (Objects.equals(businessType, BusinessTypeEnums.SCHOOL.getCode())) {
            return TemplateDemo.SCHOOL_BIZ;
        } else if (Objects.equals(businessType, BusinessTypeEnums.COMMUNITY.getCode())) {
            return TemplateDemo.COMMUNITY_BIZ;
        }
        return null;
    }
}
