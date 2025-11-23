package com.ibrahim.dubaiconciergerie.demo.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ContactRequestDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom complet est obligatoire.")
    @Size(max = 100, message = "Le nom complet ne peut pas dépasser 100 caractères.")
    private String fullName;

    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "L'email n'est pas valide.")
    @Size(max = 150, message = "L'email ne peut pas dépasser 150 caractères.")
    private String email;

    @Size(max = 100, message = "Le type de bien ne peut pas dépasser 100 caractères.")
    private String propertyType;

    @Size(max = 50, message = "Le type de location ne peut pas dépasser 50 caractères.")
    private String rentalType;

    @NotBlank(message = "Le message est obligatoire.")
    @Size(max = 2000, message = "Le message ne peut pas dépasser 2000 caractères.")
    private String message;

    private String status;

    private LocalDateTime createdAt;
}
