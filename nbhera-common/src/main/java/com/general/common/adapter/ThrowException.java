package com.general.common.adapter;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/6/21 23:53
 * desc: 允许抛出非 WithCodeException
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThrowException {
}
