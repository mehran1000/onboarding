package com.candidate.mastcheshmi.onboarding.validation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MinYearDifferenceWithNowValidatorTest {

    private MinYearDifferenceWithNowValidator validator;
    private ConstraintValidatorContext constraintAnnotation;
    private MinYearDifferenceWithNow minDiffAnnotation;

    @BeforeEach
    void setUp() {
        constraintAnnotation = mock(ConstraintValidatorContext.class);
        minDiffAnnotation = mock(MinYearDifferenceWithNow.class);
        validator = new MinYearDifferenceWithNowValidator();
    }

    @Test
    void isValid_nullDate_returnsTrue() {
        // When
        boolean validationResult = validator.isValid(null, constraintAnnotation);

        // Then
        Assertions.assertThat(validationResult)
            .isTrue();
    }

    @Test
    void isValid_dateIsAboveMinDiff_returnsTrue() {
        // Given
        LocalDate annotatedData = LocalDate.now().minusYears(20);
        when(minDiffAnnotation.value()).thenReturn(18);
        validator.initialize(minDiffAnnotation);

        // When
        boolean validationResult = validator.isValid(annotatedData, constraintAnnotation);

        // Then
        Assertions.assertThat(validationResult)
            .isTrue();
    }

    @Test
    void isValid_datIsBelowMinDiff_returnsFalse() {
        // Given
        LocalDate annotatedData = LocalDate.now().minusYears(17);
        when(minDiffAnnotation.value()).thenReturn(18);
        validator.initialize(minDiffAnnotation);

        // When
        boolean validationResult = validator.isValid(annotatedData, constraintAnnotation);

        // Then
        Assertions.assertThat(validationResult)
            .isFalse();
    }

    @Test
    void isValid_dateIsEqualToMinDiff_returnsTrue() {
        // Given
        LocalDate annotatedData = LocalDate.now().minusYears(18);
        when(minDiffAnnotation.value()).thenReturn(18);
        validator.initialize(minDiffAnnotation);

        // When
        boolean validationResult = validator.isValid(annotatedData, constraintAnnotation);

        // Then
        Assertions.assertThat(validationResult)
            .isTrue();
    }

    @Test
    void isValid_dateIsinFuture_returnsTrue() {
        // Given
        LocalDate annotatedData = LocalDate.now().plusYears(19);
        when(minDiffAnnotation.value()).thenReturn(18);
        validator.initialize(minDiffAnnotation);

        // When
        boolean validationResult = validator.isValid(annotatedData, constraintAnnotation);

        // Then
        Assertions.assertThat(validationResult)
            .isFalse();
    }
}