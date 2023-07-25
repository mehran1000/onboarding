package com.candidate.mastcheshmi.onboarding.exceptioin;

public class CountryIsNotSupportedException extends BadRequestException {

    public CountryIsNotSupportedException(String country) {
        super(String.format("The bank is not active in %s.", country));
    }
}
