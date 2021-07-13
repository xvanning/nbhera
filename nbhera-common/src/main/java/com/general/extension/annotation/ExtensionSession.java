package com.general.extension.annotation;

import java.lang.annotation.*;

/**
 * @author xvanning
 * date: 2021/5/30 20:47
 * desc: 扩展点session
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtensionSession {
}
