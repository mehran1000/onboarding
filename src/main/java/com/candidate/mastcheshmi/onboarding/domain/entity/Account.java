package com.candidate.mastcheshmi.onboarding.domain.entity;

import com.candidate.mastcheshmi.onboarding.domain.constant.AccountType;
import com.candidate.mastcheshmi.onboarding.domain.constant.IdGeneratorConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(generator = IdGeneratorConstants.GENERATOR_NAME)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountType accountType;

    @Column(columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00")
    private BigDecimal balance;

    @Column(columnDefinition = "VARCHAR(3) DEFAULT 'EUR'")
    @NotBlank
    private String currency;

    private LocalDateTime accountOpeningTimestamp;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
