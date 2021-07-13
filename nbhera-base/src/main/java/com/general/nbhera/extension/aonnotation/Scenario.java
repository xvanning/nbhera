package com.general.nbhera.extension.aonnotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/13 23:59
 * desc: 场景定义
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scenario {


}
