package com.sirma.football_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MinutesRangeValidator.class)
public @interface MinutesRange {

    String message() default ValidationMessages.MINUTES_RANGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
