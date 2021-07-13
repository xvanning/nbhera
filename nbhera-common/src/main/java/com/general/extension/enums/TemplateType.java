package com.general.extension.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xvanning
 * date: 2021/5/30 21:08
 * desc: 模板类型
 */
@AllArgsConstructor
@Getter
public enum TemplateType {

    /**
     * 垂直业务模板
     */
    VERTICAL(500, "垂直业务模板"),

    /**
     * 水平业务模板
     */
    HORIZONTAL(1000, "水平业务模板"),

    /**
     * 自由模板，不与业务绑定
     */
    FREEDOM(1500, "自由模板，不与业务绑定"),;


    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 描述
     */
    private String desc;
}
