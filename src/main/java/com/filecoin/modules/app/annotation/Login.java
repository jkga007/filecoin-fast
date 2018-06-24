package com.filecoin.modules.app.annotation;

import java.lang.annotation.*;

/**
 * app登录效验
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2017/9/23 14:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
