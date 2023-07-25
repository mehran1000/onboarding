package com.candidate.mastcheshmi.onboarding.controller;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.candidate.mastcheshmi.onboarding.constants.TestConstants;
import com.candidate.mastcheshmi.onboarding.controller.dto.AddressDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.CustomerRegistrationRequestDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.CustomerRegistrationResponseDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.LogonRequestDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.LogonResponseDto;
import com.candidate.mastcheshmi.onboarding.domain.constant.AccountType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String REGISTER_SERVICE_URI = "/client-api/v1/register";
    private static final String OVERVIEW_SERVICE_URI = "/client-api/v1/overview";
    private static final String LOGON_SERVICE_URI = "/client-api/v1/logon";
    public static final String VALIDATION_ERROR_TITLE = "Validation Error";
    public static final String VALIDATION_ERROR_TYPE = "/problems/validation-error";

    public static final String JSON_PATH_TO_ERROR_TYPE = "$.type";
    public static final String JSON_PATH_TO_ERROR_TITLE = "$.title";
    public static final String JSON_PATH_TO_ERROR_DETAIL = "detail";
    public static final String JSON_PATH_TO_FIELD_VALIDATION_ERROR = "$.fieldValidationError";

    @Test
    void registerCustomer_validaInput_successfulRegistration() throws Exception {
        registerValidCustomer(TestConstants.SOME_USER_NAME);
    }

    @Test
    void registerCustomer_ageLessThanEighteen_badRequestError() throws Exception {
        // Given
        CustomerRegistrationRequestDto registrationRequest = createCustomerRegistrationRequest("user1");
        LocalDate newBirthDate = LocalDate.now().minusYears(17);
        registrationRequest = new CustomerRegistrationRequestDto(registrationRequest.name(), newBirthDate,
            registrationRequest.idDocumentBase64(), registrationRequest.userName(), registrationRequest.address());

        String reqBody = objectMapper.writeValueAsString(registrationRequest);

        //when
        mockMvc.perform(post(REGISTER_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TYPE).value(VALIDATION_ERROR_TYPE))
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TITLE).value(VALIDATION_ERROR_TITLE))
            .andExpect(jsonPath(JSON_PATH_TO_FIELD_VALIDATION_ERROR + ".dob").value(
                "Customers above 18 year age are only allowed to register and create account."));
    }

    @Test
    void registerCustomer_emptyValueForAllAddressFields_badRequestError() throws Exception {
        // Given
        CustomerRegistrationRequestDto registrationRequest = createCustomerRegistrationRequest("user2");

        registrationRequest = new CustomerRegistrationRequestDto(registrationRequest.name(), registrationRequest.dob(),
            registrationRequest.idDocumentBase64(), registrationRequest.userName(), AddressDto.builder().build());

        String reqBody = objectMapper.writeValueAsString(registrationRequest);

        //when
        mockMvc.perform(post(REGISTER_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TYPE).value(VALIDATION_ERROR_TYPE))
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TITLE).value(VALIDATION_ERROR_TITLE))
            .andExpect(jsonPath(JSON_PATH_TO_FIELD_VALIDATION_ERROR + ".['address.country']").value(
                "The given country cannot be blank."))
            .andExpect(jsonPath(JSON_PATH_TO_FIELD_VALIDATION_ERROR + ".['address.houseNumber']").value(
                "The given house number cannot be blank."))
            .andExpect(jsonPath(JSON_PATH_TO_FIELD_VALIDATION_ERROR + ".['address.city']").value(
                "The given city cannot be blank."))
            .andExpect(jsonPath(JSON_PATH_TO_FIELD_VALIDATION_ERROR + ".['address.postalCode']").value(
                "The given postal code cannot be blank."));
    }

    @Test
    void registerCustomer_duplicatedUserName_conflictError() throws Exception {
        // Given
        registerValidCustomer("user3"); // On user is registered with "mehran" userName
        CustomerRegistrationRequestDto registrationRequest = createCustomerRegistrationRequest("user3");

        String reqBody = objectMapper.writeValueAsString(registrationRequest);

        //when
        mockMvc.perform(post(REGISTER_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isConflict())
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TYPE).value("/problems/duplicate-userName"))
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TITLE).value("Duplicate userName"))
            .andExpect(
                jsonPath(JSON_PATH_TO_ERROR_DETAIL).value("The user3 userName already exist."));
    }

    @Test
    void registerCustomer_emptyValueForAllCustomerFieldsExceptAddressAndDob_badRequestError() throws Exception {
        // Given
        CustomerRegistrationRequestDto registrationRequest = CustomerRegistrationRequestDto.builder()
            .address(createValidAddress())
            .dob(TestConstants.SOME_BIRTH_DATE)
            .build();

        String reqBody = objectMapper.writeValueAsString(registrationRequest);

        //when
        mockMvc.perform(post(REGISTER_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TYPE).value(VALIDATION_ERROR_TYPE))
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TITLE).value(VALIDATION_ERROR_TITLE))
            .andExpect(jsonPath(JSON_PATH_TO_FIELD_VALIDATION_ERROR + ".idDocumentBase64").value(
                "The given Id document cannot be blank."))
            .andExpect(jsonPath(JSON_PATH_TO_FIELD_VALIDATION_ERROR + ".name").value(
                "The given name cannot be blank."))
            .andExpect(jsonPath(JSON_PATH_TO_FIELD_VALIDATION_ERROR + ".userName").value(
                "The given userName cannot be blank."));
    }

    @Test
    void registerCustomer_notSupportedCountry_unprocessableEntityError() throws Exception {
        // Given
        CustomerRegistrationRequestDto registrationRequest = createCustomerRegistrationRequest("user4");
        AddressDto address = AddressDto.builder().country("Iran").postalCode(TestConstants.SOME_POSTAL_CODE).city(
                TestConstants.SOME_CITY)
            .houseNumber(TestConstants.SOME_HOUSE_NUMBER).build();
        registrationRequest = new CustomerRegistrationRequestDto(registrationRequest.name(), registrationRequest.dob(),
            registrationRequest.idDocumentBase64(), registrationRequest.userName(), address);

        String reqBody = objectMapper.writeValueAsString(registrationRequest);

        //when
        mockMvc.perform(post(REGISTER_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TYPE).value("/problems/not-supported-country"))
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TITLE).value("Not supported country"))
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_DETAIL).value("The bank is not active in Iran."));
    }

    @Test
    void logon_validaCredential_successfulLogon() throws Exception {
        registerValidCustomer("user8");
    }

    @Test
    void logon_invalidUserName_successfulLogon() throws Exception {
        // Given
        var logonRequest = new LogonRequestDto("test", "test");
        String reqBody = objectMapper.writeValueAsString(logonRequest);

        // When
        mockMvc.perform(post(LOGON_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TITLE).value("Invalid credential"))
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_DETAIL).value("The userName and/or password are invalid."));
    }

    @Test
    void logon_invalidPassword_successfulLogon() throws Exception {
        // Given
        CustomerRegistrationResponseDto customerRegistrationResponseDto = registerValidCustomer("user9");
        var logonRequest = new LogonRequestDto(customerRegistrationResponseDto.userName(), "invalidPass");
        String reqBody = objectMapper.writeValueAsString(logonRequest);

        // When
        mockMvc.perform(post(LOGON_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_TITLE).value("Invalid credential"))
            .andExpect(jsonPath(JSON_PATH_TO_ERROR_DETAIL).value("The userName and/or password are invalid."));
    }

    @Test
    void overview_authenticatedUser_overview() throws Exception {
        // When
        mockMvc.perform(get(OVERVIEW_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON))
            // Then
            .andExpect(status().isForbidden());
    }

    @Test
    void overview_unauthenticatedUser_overView() throws Exception {
        // Give
        LogonResponseDto logOnResponse = logon("user10");

        // When
        mockMvc.perform(get(OVERVIEW_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + logOnResponse.accessToken()))
            // Then
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(notNullValue()))
            .andExpect(jsonPath("$.name").value(notNullValue()))
            .andExpect(jsonPath("$.accounts[0].accountNumber").value(notNullValue()))
            .andExpect(jsonPath("$.accounts[0].accountType").value(AccountType.CURRENT.toString()));
    }

    private LogonResponseDto logon(String userName) throws Exception {
        // Given
        CustomerRegistrationResponseDto customerRegistrationResponseDto = registerValidCustomer("user11");
        var logonRequest = new LogonRequestDto(customerRegistrationResponseDto.userName(),
            customerRegistrationResponseDto.password());
        String reqBody = objectMapper.writeValueAsString(logonRequest);

        //when
        String response = mockMvc.perform(post(LOGON_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, LogonResponseDto.class);
    }

    private CustomerRegistrationResponseDto registerValidCustomer(String userName) throws Exception {
        // Given
        CustomerRegistrationRequestDto registrationRequest = createCustomerRegistrationRequest(userName);
        String reqBody = objectMapper.writeValueAsString(registrationRequest);

        //when
        String response = mockMvc.perform(post(REGISTER_SERVICE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqBody))
            // Then
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, CustomerRegistrationResponseDto.class);
    }

    private static CustomerRegistrationRequestDto createCustomerRegistrationRequest(String userName) {
        var registrationRequest = CustomerRegistrationRequestDto.builder()
            .address(createValidAddress())
            .dob(TestConstants.SOME_BIRTH_DATE)
            .name(TestConstants.SOME_NAME)
            .userName(userName)
            .idDocumentBase64(TestConstants.SOME_DOCUMENT_ID).build();
        return registrationRequest;
    }

    private static AddressDto createValidAddress() {
        return AddressDto.builder()
            .country(TestConstants.SOME_COUNTRY)
            .city(TestConstants.SOME_POSTAL_CODE)
            .houseNumber(TestConstants.SOME_HOUSE_NUMBER)
            .postalCode(TestConstants.SOME_POSTAL_CODE)
            .build();
    }
}