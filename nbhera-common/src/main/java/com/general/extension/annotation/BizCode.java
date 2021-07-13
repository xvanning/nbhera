package com.general.extension.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/30 20:47
 * desc: 表示当前字段为业务code
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizCode {
}
