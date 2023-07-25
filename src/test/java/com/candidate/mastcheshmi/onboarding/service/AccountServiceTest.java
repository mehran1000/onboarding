package com.candidate.mastcheshmi.onboarding.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.candidate.mastcheshmi.onboarding.domain.constant.AccountType;
import com.candidate.mastcheshmi.onboarding.domain.entity.Account;
import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import com.candidate.mastcheshmi.onboarding.exceptioin.CustomerNotFoundException;
import com.candidate.mastcheshmi.onboarding.repository.AccountRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    public static final String CURRENCY_EURO = "EUR";

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountForCustomer_validCustomerId_callsSaveMethod() {
        // Given
        var customer = Customer.builder().id(1l).build();
        when(customerService.findById(1l)).thenReturn(customer);
        ArgumentCaptor<Account> newAccountCaptor = ArgumentCaptor.forClass(Account.class);

        // When
        accountService.createAccount(1l, AccountType.CURRENT, CURRENCY_EURO);

        // Then
        verify(accountRepository, times(1)).save(newAccountCaptor.capture());
        Account newAccount = newAccountCaptor.getValue();
        assertThat(newAccount.getAccountNumber()).isNotNull();
        assertThat(newAccount.getCustomer().getId()).isEqualTo(1l);
        assertThat(newAccount.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(newAccount.getAccountType()).isEqualTo(AccountType.CURRENT);
        assertThat(newAccount.getCurrency()).isEqualTo(CURRENCY_EURO);
    }

    @Test
    void createAccountForCustomer_inValidCustomerId_throwsCustomerNotFoundException() {
        // Given
        var customer = Customer.builder().id(1l).build();
        when(customerService.findById(1l)).thenThrow(CustomerNotFoundException.class);

        // When/Then
        assertThatThrownBy(() ->accountService.createAccount(1l, AccountType.CURRENT, CURRENCY_EURO))
            .isInstanceOf(CustomerNotFoundException.class);
    }
}