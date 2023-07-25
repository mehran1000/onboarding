package com.candidate.mastcheshmi.onboarding.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address extends BaseEntity {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String houseNumber;

    private String extraAddressLine;
}
