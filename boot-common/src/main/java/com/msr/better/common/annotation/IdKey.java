package com.msr.better.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-06-15 23:08
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface IdKey {
    String key() default "";
    String value() default "";
}
