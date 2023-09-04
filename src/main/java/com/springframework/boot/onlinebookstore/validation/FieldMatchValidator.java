package com.springframework.boot.onlinebookstore.validation;

import com.springframework.boot.onlinebookstore.exception.FieldValueGetterException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstObject = FieldValueGetter.getFieldValue(value, firstFieldName);
            Object secondObject = FieldValueGetter.getFieldValue(value, secondFieldName);
            if (firstObject == null && secondObject == null) {
                return true;
            }
            return firstObject != null && firstObject.equals(secondObject);
        } catch (Exception e) {
            throw new FieldValueGetterException("Can't get value of password "
                    + "or/and repeatPassword", e);
        }
    }
}
