package com.candidate.mastcheshmi.onboarding.service;

import com.candidate.mastcheshmi.onboarding.config.PasswordGenerationPoliciesProperties;
import lombok.RequiredArgsConstructor;
import org.passay.AllowedCharacterRule;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Service;

/**
 * This service class provides functionality for generating secure passwords based on the password generation policies
 * provided.
 */
@Service
@RequiredArgsConstructor
public class SecurePasswordGeneratorService {

    private final PasswordGenerationPoliciesProperties passwordGenerationPoliciesProperties;

    /**
     * Generates a secure password based on the configured password generation policies.
     *
     * @return A randomly generated secure password
     */
    public String generatePassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(passwordGenerationPoliciesProperties.getLowerCaseCount());

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(passwordGenerationPoliciesProperties.getUpperCaseCount());

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(passwordGenerationPoliciesProperties.getDigitCount());

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return AllowedCharacterRule.ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };

        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(passwordGenerationPoliciesProperties.getSpecialCharCount());

        return gen.generatePassword(passwordGenerationPoliciesProperties.getLength(), splCharRule, lowerCaseRule,
            upperCaseRule, digitRule);
    }
}
