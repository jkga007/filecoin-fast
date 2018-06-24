package com.filecoin.datasources.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2017/9/16 22:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default "";
}
