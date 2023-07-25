package com.candidate.mastcheshmi.onboarding.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A data class representing a supported country configuration.
 */
@Data
public class CountryConfig {

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotBlank
    private String currency;
}
