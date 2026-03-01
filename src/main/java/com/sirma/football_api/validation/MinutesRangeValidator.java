package com.sirma.football_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;

public class MinutesRangeValidator implements ConstraintValidator<MinutesRange, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            Integer from = invoke(value, "getFromMinutes");
            Integer to = invoke(value, "getToMinutes");
            if (from == null || to == null) {
                return true;
            }
            return from <= to;
        } catch (Exception e) {
            return false;
        }
    }

    private static Integer invoke(Object obj, String getterName) throws Exception {
        Method m = obj.getClass().getMethod(getterName);
        Object result = m.invoke(obj);
        return result instanceof Integer ? (Integer) result : null;
    }
}
