package com.ibrahim.dubaiconciergerie.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Builder
public class ContactRequestResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private String propertyType;   // même type que dans ContactRequestDto
    private String rentalType;     // idem
    private String message;
    private String status;  // enum dans ton entity
    private LocalDateTime createdAt;
}

