package com.candidate.mastcheshmi.onboarding.controller.dto;

import com.candidate.mastcheshmi.onboarding.validation.MinYearDifferenceWithNow;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;

@Builder
@Schema(description = "The registration request.")
public record CustomerRegistrationRequestDto(
    @NotBlank(message = "The given name cannot be blank.")
    @Schema(description = "The name of the customer.", example = "Mehran Mastcheshmi")
    String name,

    @MinYearDifferenceWithNow(value = 18, message = "Customers above 18 year age are only allowed to register and create account.")
    @Schema(description = "The date of birth of the customer.", example = "1987-05-09")
    LocalDate dob,

    @NotBlank(message = "The given Id document cannot be blank.")
    @Schema(description = "The Base64 encoded Id document of the customer.", example = "PGh0bWwgbGFuZz0iZGUi...")
    String idDocumentBase64,

    @NotBlank(message = "The given userName cannot be blank.")
    @Schema(description = "The username of the customer.", example = "mastcheshmi")
    String userName,

    @Valid
    @NotNull(message = "The given address cannot be null.")
    @Schema(description = "The address details of the customer.")
    AddressDto address
) {

}
