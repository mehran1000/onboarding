package com.candidate.mastcheshmi.onboarding.config;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This configuration properties specifies the requirements for generating secure passwords.
 */
@Component
@ConfigurationProperties(prefix = "abs-bank.password.generation.policies")
@Data
public class PasswordGenerationPoliciesProperties {

    @Min(0)
    private int lowerCaseCount;

    @Min(0)
    private int upperCaseCount;

    @Min(0)
    private int specialCharCount;

    @Min(0)
    private int digitCount;

    @Min(1)
    private int length;
}
