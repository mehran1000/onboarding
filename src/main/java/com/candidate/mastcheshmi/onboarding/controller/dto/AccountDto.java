package com.candidate.mastcheshmi.onboarding.controller.dto;

import com.candidate.mastcheshmi.onboarding.domain.constant.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "An object representing an individual customer account.")
public record AccountDto(
    @Schema(description = "The account number of the account.", example = "NL91ABCA0417164300")
    String accountNumber,

    @Schema(description = "The accounts type of the account.", example = "CURRENT")
    AccountType accountType,

    @Schema(description = "The currency of the account.", example = "EUR")
    String currency,

    @Schema(description = "The balance of the account.", example = "100.25")
    BigDecimal balance,

    @Schema(description = "The date and time of the account opening.", example = "2023-07-24T17:01:14.438034")
    LocalDateTime accountOpeningTimestamp
) {

}
