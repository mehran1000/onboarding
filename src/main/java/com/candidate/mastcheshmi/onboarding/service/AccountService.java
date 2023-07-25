package com.candidate.mastcheshmi.onboarding.service;

import com.candidate.mastcheshmi.onboarding.domain.constant.AccountType;
import com.candidate.mastcheshmi.onboarding.domain.entity.Account;
import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import com.candidate.mastcheshmi.onboarding.repository.AccountRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

/**
 * This service class provides functionality to create and manage accounts for customers.
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;

    /**
     * Creates a new account for a given customer with the specified account type and currency.
     *
     * @param customerId  The ID of the customer for whom the account is created.
     * @param accountType The type of the account
     * @param currency    The currency of the account
     * @return The newly created account.
     */
    public Account createAccount(Long customerId, AccountType accountType, String currency) {
        Customer customer = customerService.findById(customerId);
        var account = Account.builder()
            .accountNumber(generateIban())
            .accountOpeningTimestamp(LocalDateTime.now())
            .accountType(accountType)
            .balance(BigDecimal.ZERO)
            .currency(currency)
            .customer(customer)
            .build();

        return accountRepository.save(account);
    }

    /**
     * Generates a random IBAN (International Bank Account Number) for the account.
     *
     * @return A randomly generated IBAN.
     */
    private String generateIban() {
        return new Iban.Builder()
            .countryCode(CountryCode.NL)
            .bankCode("ABCA")
            .buildRandom().toString();
    }
}
