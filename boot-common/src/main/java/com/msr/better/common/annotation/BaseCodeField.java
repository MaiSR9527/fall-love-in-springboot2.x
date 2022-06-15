package com.msr.better.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-06-15 23:05
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BaseCodeField {

    /**
     * 批处理条件CODE值
     */
    String value();

    /**
     * 日期格式化, 仅支持字段类型为日期
     */
    String format() default "yyyy-MM-dd HH:mm:sss";

    /**
     * 字段分隔符, 仅支持字段类型为集合, eg:1,2,3->[1,2,3]
     */
    String separator() default ",";

}
