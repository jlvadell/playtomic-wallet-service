package com.playtomic.tests.application.validation.impl;

import com.playtomic.tests.application.validation.NotZero;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotZeroValidator implements ConstraintValidator<NotZero, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value != 0;
    }
}
