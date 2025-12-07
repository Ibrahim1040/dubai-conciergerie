package com.ibrahim.dubaiconciergerie.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// ce que le front envoie pour créer/modifier
public class PropertyRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    @Min(1)
    private Integer capacity;

    @NotNull
    private String rentalType; // "DAILY", "MONTHLY", ...

    @Positive
    private BigDecimal nightlyPrice;

    @PositiveOrZero
    private BigDecimal monthlyPrice;



    // getters / setters
}

