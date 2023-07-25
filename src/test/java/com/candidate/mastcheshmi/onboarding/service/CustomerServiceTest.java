package com.candidate.mastcheshmi.onboarding.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.candidate.mastcheshmi.onboarding.config.SupportedCountriesProperties;
import com.candidate.mastcheshmi.onboarding.constants.TestConstants;
import com.candidate.mastcheshmi.onboarding.domain.entity.Address;
import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import com.candidate.mastcheshmi.onboarding.event.CustomerRegistrationEvent;
import com.candidate.mastcheshmi.onboarding.exceptioin.CountryIsNotSupportedException;
import com.candidate.mastcheshmi.onboarding.exceptioin.CustomerNotFoundException;
import com.candidate.mastcheshmi.onboarding.exceptioin.InvalidCredentialException;
import com.candidate.mastcheshmi.onboarding.exceptioin.UserAlreadyExistException;
import com.candidate.mastcheshmi.onboarding.exceptioin.UserNameNotFoundException;
import com.candidate.mastcheshmi.onboarding.repository.CustomerRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private SecurePasswordGeneratorService securePasswordGeneratorService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private SupportedCountriesProperties supportedCountriesProperties;
    @InjectMocks
    private CustomerService customerService;

    @Test
    void register_validInput_callsSaveAndPublishesEvent() {
        // Given
        var customer = Customer.builder().userName(TestConstants.SOME_USER_NAME).id(TestConstants.SOME_CUSTOMER_ID)
            .address(Address.builder().country(TestConstants.SOME_COUNTRY).build())
            .build();
        when(customerRepository.findByUserName(any())).thenReturn(Optional.ofNullable(null));
        when(supportedCountriesProperties.getSupportedCountryNames()).thenReturn(Set.of(TestConstants.SOME_COUNTRY));
        when(securePasswordGeneratorService.generatePassword()).thenReturn(TestConstants.SOME_PASSWORD);
        when(passwordEncoder.encode(TestConstants.SOME_PASSWORD)).thenReturn(TestConstants.SOME_PASSWORD);
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        ArgumentCaptor<CustomerRegistrationEvent> customerRegistrationEventCaptor = ArgumentCaptor.forClass(
            CustomerRegistrationEvent.class);

        // When
        customerService.register(customer);

        // Then
        verify(customerRepository, times(1)).save(customerCaptor.capture());
        verify(eventPublisher, times(1)).publishEvent(customerRegistrationEventCaptor.capture());
        Customer savedCustomer = customerCaptor.getValue();
        assertThat(savedCustomer.getId()).isEqualTo(TestConstants.SOME_CUSTOMER_ID);
        assertThat(savedCustomer.getUserName()).isEqualTo(TestConstants.SOME_USER_NAME);
        assertThat(savedCustomer.getPassword()).isEqualTo(TestConstants.SOME_PASSWORD);
        assertThat(savedCustomer.getPlainPassword()).isEqualTo(TestConstants.SOME_PASSWORD);
        assertThat(savedCustomer.getAddress().getCountry()).isEqualTo(TestConstants.SOME_COUNTRY);
        CustomerRegistrationEvent registrationEvent = customerRegistrationEventCaptor.getValue();
        assertThat(registrationEvent.getCustomer().getId()).isEqualTo(TestConstants.SOME_CUSTOMER_ID);
    }

    @Test
    void register_duplicateUserName_throwsUserAlreadyExistException() {
        // Given
        var customer = Customer.builder().userName(TestConstants.SOME_USER_NAME).build();
        when(customerRepository.findByUserName(any())).thenReturn(Optional.ofNullable(customer));

        // When/Then
        assertThatThrownBy(() -> customerService.register(customer))
            .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    void register_unsupportedCountry_throwsCountryIsNotSupportedException() {
        // Given
        var customer = Customer.builder().userName(TestConstants.SOME_USER_NAME)
            .address(Address.builder().country(TestConstants.SOME_COUNTRY).build())
            .build();
        when(customerRepository.findByUserName(any())).thenReturn(Optional.ofNullable(null));
        when(supportedCountriesProperties.getSupportedCountryNames()).thenReturn(Set.of());

        // When/Then
        assertThatThrownBy(() -> customerService.register(customer))
            .isInstanceOf(CountryIsNotSupportedException.class);
    }

    @Test
    void findById_validCustomerId_returnsCustomer() {
        // Given
        var customer = Customer.builder().userName(TestConstants.SOME_USER_NAME).id(TestConstants.SOME_CUSTOMER_ID).build();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        // When
        Customer foundCustomer = customerService.findById(TestConstants.SOME_CUSTOMER_ID);

        // Then
        assertThat(foundCustomer.getId()).isEqualTo(TestConstants.SOME_CUSTOMER_ID);
        assertThat(foundCustomer.getUserName()).isEqualTo(TestConstants.SOME_USER_NAME);
    }

    @Test
    void findById_invalidCustomerId_throwsCustomerNotFoundException() {
        // Given
        var customer = Customer.builder().userName(TestConstants.SOME_USER_NAME).id(TestConstants.SOME_CUSTOMER_ID).build();
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        // When/Then
        assertThatThrownBy(() -> customerService.findById(TestConstants.SOME_CUSTOMER_ID))
            .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void authenticate_validInput_callsGenerateToken() {
        // Given
        var customer = Customer.builder().userName(TestConstants.SOME_USER_NAME).id(TestConstants.SOME_CUSTOMER_ID).password(
            TestConstants.SOME_PASSWORD).build();
        when(customerRepository.findByUserName(TestConstants.SOME_USER_NAME)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(TestConstants.SOME_PASSWORD, TestConstants.SOME_PASSWORD)).thenReturn(true);

        // When
        customerService.authenticate(TestConstants.SOME_USER_NAME, TestConstants.SOME_PASSWORD);

        // Then
        verify(tokenService, times(1)).generateToken(customer);
    }

    @Test
    void authenticate_invalidUserName_throwsInvalidCredentialException() {
        // Given
        when(customerRepository.findByUserName(TestConstants.SOME_USER_NAME)).thenReturn(Optional.ofNullable(null));

        // When/Then
        assertThatThrownBy(() -> customerService.authenticate(TestConstants.SOME_USER_NAME, TestConstants.SOME_PASSWORD))
            .isInstanceOf(InvalidCredentialException.class);
    }

    @Test
    void authenticate_invalidPassword_throwsInvalidCredentialException() {
        // Given
        var customer = Customer.builder().userName(TestConstants.SOME_USER_NAME).id(TestConstants.SOME_CUSTOMER_ID).password(
            TestConstants.SOME_PASSWORD).build();
        when(customerRepository.findByUserName(TestConstants.SOME_USER_NAME)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(TestConstants.SOME_PASSWORD, TestConstants.SOME_PASSWORD)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> customerService.authenticate(TestConstants.SOME_USER_NAME, TestConstants.SOME_PASSWORD))
            .isInstanceOf(InvalidCredentialException.class);
    }

    @Test
    void findByUserName_validUserName_returnsCustomer() {
        // Given
        var customer = Customer.builder().userName(TestConstants.SOME_USER_NAME).build();
        when(customerRepository.findByUserName(TestConstants.SOME_USER_NAME)).thenReturn(Optional.of(customer));

        // Given
        Customer foundCustomer = customerService.findByUserName(TestConstants.SOME_USER_NAME);

        // Then
        assertThat(foundCustomer.getUserName()).isEqualTo(TestConstants.SOME_USER_NAME);
    }

    @Test
    void findByUserName_invalidUserName_throwsUserNameNotFoundException() {
        // Given
        when(customerRepository.findByUserName(TestConstants.SOME_USER_NAME)).thenReturn(Optional.ofNullable(null));

        // When/Then
        assertThatThrownBy(() -> customerService.findByUserName(TestConstants.SOME_USER_NAME))
            .isInstanceOf(UserNameNotFoundException.class);
    }
}