package com.candidate.mastcheshmi.onboarding.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "An object representing the customer address.")
public record AddressDto(
    @NotBlank(message = "The given country cannot be blank.")
    @Schema(description = "The country of the address.", example = "The Netherlands")
    String country,

    @NotBlank(message = "The given city cannot be blank.")
    @Schema(description = "The city of the address.", example = "Almere")
    String city,

    @NotBlank(message = "The given postal code cannot be blank.")
    @Schema(description = "The postal code of the address.", example = "1325DH")
    String postalCode,

    @NotBlank(message = "The given house number cannot be blank.")
    @Schema(description = "The house number of the address.", example = "60")
    String houseNumber,

    @Schema(description = "Any additional information for the address.", example = "On third floor")
    String extraAddressLine
) {

}
