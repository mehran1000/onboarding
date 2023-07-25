package com.candidate.mastcheshmi.onboarding.config;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * This configuration properties holds the list of supported countries and their currencies.
 */
@Component
@ConfigurationProperties(prefix = "abs-bank")
@Validated
public class SupportedCountriesProperties {

    private List<CountryConfig> supportedCountries;
    private Set<String> supportedCountryNames = new HashSet<>();
    private Set<String> supportedCurrencies = new HashSet<>();

    /**
     * Sets the list of supported countries and updates the associated country names and currencies to provide faster
     * searche on countries and currencies.
     *
     * @param supportedCountries The list of supported countries read from application.yml
     */
    public void setSupportedCountries(
        List<@Valid CountryConfig> supportedCountries) {
        this.supportedCountries = supportedCountries;
        supportedCountryNames = supportedCountries.stream().map(CountryConfig::getName).collect(Collectors.toSet());
        supportedCurrencies = supportedCountries.stream().map(CountryConfig::getCurrency).collect(Collectors.toSet());
    }

    public List<CountryConfig> getSupportedCountries() {
        return supportedCountries;
    }

    public Set<String> getSupportedCountryNames() {
        return supportedCountryNames;
    }

    public Set<String> getSupportedCurrencies() {
        return supportedCurrencies;
    }
}
