package com.candidate.mastcheshmi.onboarding.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

/**
 * Custom validator to enforce the constraint specified by the {@link MinYearDifferenceWithNow} annotation.
 */
public class MinYearDifferenceWithNowValidator implements ConstraintValidator<MinYearDifferenceWithNow, LocalDate> {
    private int minDiff;

    @Override
    public void initialize(MinYearDifferenceWithNow constraintAnnotation) {
        this.minDiff = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate dateVal, ConstraintValidatorContext context) {
        if (dateVal == null) {
            return true; // Null values are handled by @NotNull annotation
        }

        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(dateVal, currentDate);
        return period.getYears() >= minDiff;
    }
}
