package com.candidate.mastcheshmi.onboarding.domain.entity;

import com.candidate.mastcheshmi.onboarding.domain.constant.IdGeneratorConstants;
import com.candidate.mastcheshmi.onboarding.validation.MinYearDifferenceWithNow;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(generator = IdGeneratorConstants.GENERATOR_NAME)
    private Long id;

    @NotBlank
    private String name;

    @MinYearDifferenceWithNow(value = 18, message = "Customers above 18 year age are only allowed to register and create account")
    @Past(message = "The date of birth can not be after the current date.")
    private LocalDate dob;

    @NotBlank
    private String idDocumentBase64;

    @NotBlank
    @Column(unique = true)
    private String userName;

    private String password;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;

    @Transient
    private String plainPassword;
}
