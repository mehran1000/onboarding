package com.candidate.mastcheshmi.onboarding.service;

import com.candidate.mastcheshmi.onboarding.config.SupportedCountriesProperties;
import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import com.candidate.mastcheshmi.onboarding.event.CustomerRegistrationEvent;
import com.candidate.mastcheshmi.onboarding.exceptioin.CountryIsNotSupportedException;
import com.candidate.mastcheshmi.onboarding.exceptioin.CustomerNotFoundException;
import com.candidate.mastcheshmi.onboarding.exceptioin.InvalidCredentialException;
import com.candidate.mastcheshmi.onboarding.exceptioin.UserAlreadyExistException;
import com.candidate.mastcheshmi.onboarding.exceptioin.UserNameNotFoundException;
import com.candidate.mastcheshmi.onboarding.repository.CustomerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides operations related to customers, including registration, authentication, and retrieval.
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final SecurePasswordGeneratorService securePasswordGeneratorService;
    private final ApplicationEventPublisher eventPublisher;
    private final SupportedCountriesProperties supportedCountriesProperties;

    /**
     * Registers a new customer and creates an associated account. The account creation and registration are performed
     * in one transaction for data consistency.
     *
     * @param newCustomer The Customer object representing the new customer to be registered.
     * @return The registered Customer object with the associated account.
     * @throws UserAlreadyExistException      If a customer with the same username already exists.
     * @throws CountryIsNotSupportedException If the country in the customer's address is not supported.
     */
    @Transactional
    public Customer register(Customer newCustomer) {
        validateCustomer(newCustomer);

        String pass = securePasswordGeneratorService.generatePassword();
        newCustomer.setPassword(passwordEncoder.encode(pass));
        newCustomer.setPlainPassword(pass);
        newCustomer.getAddress().setCustomer(newCustomer);
        customerRepository.save(newCustomer);
        eventPublisher.publishEvent(new CustomerRegistrationEvent(this, newCustomer));
        return newCustomer;
    }

    private void validateCustomer(Customer newCustomer) {
        Optional<Customer> customer = customerRepository.findByUserName(newCustomer.getUserName());
        if (customer.isPresent()) {
            throw new UserAlreadyExistException(newCustomer.getUserName());
        }

        if (!supportedCountriesProperties.getSupportedCountryNames().contains(newCustomer.getAddress().getCountry())) {
            throw new CountryIsNotSupportedException(newCustomer.getAddress().getCountry());
        }
    }

    /**
     * Finds a customer by their unique identifier (customerId).
     *
     * @param customerId The unique identifier of the customer to be retrieved.
     * @return The Customer object corresponding to the provided customerId.
     * @throws CustomerNotFoundException If no customer is found with the given customerId.
     */
    public Customer findById(long customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    /**
     * Authenticates a customer by verifying the provided username and password.
     *
     * @param userName The username of the customer
     * @param password The password of the customer
     * @return The authentication token generated for the authenticated customer.
     * @throws InvalidCredentialException If the provided username or password is incorrect.
     */
    public String authenticate(String userName, String password) {
        Optional<Customer> customer = customerRepository.findByUserName(userName);
        if (!customer.isPresent()) {
            throw new InvalidCredentialException();
        }

        if (!this.passwordEncoder.matches(password, customer.get().getPassword())) {
            throw new InvalidCredentialException();
        }

        return tokenService.generateToken(customer.get());
    }

    /**
     * Finds a customer by their unique userName.
     *
     * @param userName The username of the customer to be retrieved.
     * @return The Customer object corresponding to the provided username.
     * @throws UserNameNotFoundException If no customer is found with the given username.
     */
    public Customer findByUserName(String userName) {
        return customerRepository.findByUserName(userName)
            .orElseThrow(() -> new UserNameNotFoundException(userName));
    }
}
