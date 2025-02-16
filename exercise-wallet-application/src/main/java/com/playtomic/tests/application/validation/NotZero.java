package com.playtomic.tests.application.validation;

import com.playtomic.tests.application.validation.impl.NotZeroValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotZeroValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotZero {
    String message() default "Value must not be zero";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
