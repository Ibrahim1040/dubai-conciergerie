package com.ibrahim.dubaiconciergerie.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PropertyDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    @Min(1)
    private int capacity;

    @NotBlank
    private String rentalType; // SHORT_TERM ou LONG_TERM

    private BigDecimal nightlyPrice;
    private BigDecimal monthlyPrice;

    private Long ownerId; // très important
}
