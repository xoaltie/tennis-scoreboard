package com.github.xoaltie.tennisscoreboard.validation;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Size(min = 1, max = 50)
@Pattern(regexp = "^(?=[^\\p{L}]*\\p{L})[\\p{L} '-]+$")
@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface PlayerName {
    @OverridesAttribute(constraint = Pattern.class, name = "message")
    String message() default "must contain at least one letter and only letters, spaces, hyphens, or apostrophes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
