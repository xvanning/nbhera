package com.general.nbhera.extension.aonnotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/14 0:03
 * desc: 策略模式扩展点
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Strategy {
}
