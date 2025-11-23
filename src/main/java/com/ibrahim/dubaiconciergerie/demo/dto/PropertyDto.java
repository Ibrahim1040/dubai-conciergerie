package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyDto {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotNull
    private Property.RentalType rentalType;

    @Min(0)
    private Double nightlyPrice;

    @Min(0)
    private Double monthlyPrice;

    // Optionnel : si tu veux lier à un owner précis
    private Long ownerId;
}
