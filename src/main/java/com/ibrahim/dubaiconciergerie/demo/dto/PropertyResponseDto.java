package com.ibrahim.dubaiconciergerie.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PropertyResponseDto {
    private Long id;
    private String title;
    private String city;
    private String address;
    private int capacity;
    private String rentalType;
    private BigDecimal nightlyPrice;
    private BigDecimal monthlyPrice;
    private Long ownerId; // toujours utile !
}
