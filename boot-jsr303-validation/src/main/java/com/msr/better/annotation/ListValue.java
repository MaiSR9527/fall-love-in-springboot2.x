package com.msr.better.annotation;

import com.msr.better.validate.ListValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-17
 */
@Documented
@Constraint(
        validatedBy = {ListValueValidator.class}
)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE,LOCAL_VARIABLE,TYPE_PARAMETER })
@Retention(RUNTIME)
public @interface ListValue {
    String message() default "{com.msr.better.annotation.ListValue.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int[] list() default {};
}
