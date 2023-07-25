package com.candidate.mastcheshmi.onboarding.controller;

import com.candidate.mastcheshmi.onboarding.controller.dto.CustomerOverviewResponseDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.CustomerRegistrationRequestDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.CustomerRegistrationResponseDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.LogonRequestDto;
import com.candidate.mastcheshmi.onboarding.controller.dto.LogonResponseDto;
import com.candidate.mastcheshmi.onboarding.controller.mapper.CustomerRegistrationMapper;
import com.candidate.mastcheshmi.onboarding.domain.entity.Customer;
import com.candidate.mastcheshmi.onboarding.exceptioin.ValidationProblemDetail;
import com.candidate.mastcheshmi.onboarding.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client-api/v1")
@Tag(name = "Customer Management")
@Validated
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRegistrationMapper customerRegistrationMapper;

    @Operation(summary = "Register a customer with basic details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully registered the customer.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerRegistrationResponseDto.class))}),
        @ApiResponse(responseCode = "400", description = "Bad Request: Some of the given fields are invalid.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationProblemDetail.class))}),
        @ApiResponse(responseCode = "422", description = "Unprocessable Entity: The given country is not supported.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
        @ApiResponse(responseCode = "409", description = "Conflict: The given userName is already registered.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})})
    @PostMapping("/register")
    public ResponseEntity<CustomerRegistrationResponseDto> registerCustomer(
        @RequestBody @Valid CustomerRegistrationRequestDto registrationRequest) {
        Customer customer = customerRegistrationMapper.mapToCustomer(registrationRequest);
        return ResponseEntity.ok(
            customerRegistrationMapper.mapToRegistrationResponse(customerService.register(customer)));
    }

    @Operation(summary = "Authenticate the given credentials and issue a JWT token if they input is valid.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated the customer.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LogonResponseDto.class))}),
        @ApiResponse(responseCode = "400", description = "Bad Request: Some of the given fields are invalid.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationProblemDetail.class))}),
        @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})})
    @PostMapping("/logon")
    public ResponseEntity<LogonResponseDto> logon(
        @RequestBody @Valid LogonRequestDto logonRequestDto) {
        return ResponseEntity.ok(
            new LogonResponseDto(customerService.authenticate(logonRequestDto.userName(), logonRequestDto.password())));
    }

    @Operation(summary = "Provides customer's account overview")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the account overview.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LogonResponseDto.class))}),
        @ApiResponse(responseCode = "403", description = "Forbidden: Authorization error.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})})
    @GetMapping("/overview")
    public ResponseEntity<CustomerOverviewResponseDto> overview(Principal principal) {
        return ResponseEntity.ok(
            customerRegistrationMapper.mapToCustomerOverview(customerService.findByUserName(principal.getName())));
    }
}
