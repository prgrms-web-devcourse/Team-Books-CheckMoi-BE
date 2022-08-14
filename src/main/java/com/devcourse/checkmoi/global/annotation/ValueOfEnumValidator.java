package com.devcourse.checkmoi.global.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    private ValueOfEnum annotation;

    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Object[] enumValues = this.annotation.codeMappingEnumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.equals(((CodeMappable) enumValue).getMappingCode())) {
                    return true;
                }
            }
        }
        return false;
    }

}