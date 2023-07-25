package com.candidate.mastcheshmi.onboarding.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The difference between the current date and annotated {@link java.time.LocalDate} filed must be higher or equal to
 * the specified value.
 * <br>
 * <strong>Note: The default value is 0.</strong>
 */
@Documented
@Constraint(validatedBy = MinYearDifferenceWithNowValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinYearDifferenceWithNow {

    String message() default "Customer must be at least {value} years old";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value() default 0;
}
