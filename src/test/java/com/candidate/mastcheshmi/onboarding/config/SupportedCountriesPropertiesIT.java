package com.candidate.mastcheshmi.onboarding.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SupportedCountriesPropertiesIT {

    @Autowired
    private SupportedCountriesProperties supportedCountriesProperties;

    @Test
    void getCountries_validConfigInYmlFile_returnsDefinedCountries() {
        List<CountryConfig> countries = supportedCountriesProperties.getSupportedCountries();
        Assertions.assertThat(countries)
            .hasSize(2)
            .extracting(CountryConfig::getName, CountryConfig::getCode, CountryConfig::getCurrency)
            .containsExactlyInAnyOrder(
                tuple( "The Netherlands",  "NL", "EUR"),
                tuple( "Belgium",  "BE", "EUR")
            );
    }

    @Test
    void getSupportedCountryNames_validConfigInYmlFil_returnsDefinedNames() {
        Set<String> supportedCountryNames = supportedCountriesProperties.getSupportedCountryNames();
        assertThat(supportedCountryNames)
            .containsExactlyInAnyOrder("The Netherlands", "Belgium");
    }

    @Test
    void getSupportedCurrencies_validConfigInYmlFil_returnsDefinedCurrencies() {
        Set<String> supportedCurrencies = supportedCountriesProperties.getSupportedCurrencies();
        assertThat(supportedCurrencies)
            .containsExactlyInAnyOrder("EUR");
    }
}