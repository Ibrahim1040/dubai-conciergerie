package com.ibrahim.dubaiconciergerie.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BookingDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "propertyId obligatoire")
    private Long propertyId;

    @NotBlank(message = "Nom du client obligatoire")
    private String guestName;

    @Email(message = "Email invalide")
    @NotBlank(message = "Email du client obligatoire")
    private String guestEmail;

    @NotNull(message = "checkIn obligatoire")
    private LocalDate startDate;

    @NotNull(message = "checkOut obligatoire")
    private LocalDate endDate;

    @NotNull(message = "Prix total obligatoire")
    private Double totalPrice;

    @Pattern(regexp = "PENDING|CONFIRMED|CANCELED",
            message = "Status invalide")
    private String status;
}
