package com.ibrahim.dubaiconciergerie.demo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "ID généré par la base",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "1"
    )
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2,max = 80)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2,max = 80)
    private String lastName;

    @NotBlank(message = "Email ne peut pas être vide")
    @Email(message = "Email doit être valide")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100,message = "Password must be at least 6 characters")
    @Schema(
            description = "Mot de passe (seulement à la création / mise à jour)",
            accessMode = Schema.AccessMode.WRITE_ONLY,
            example = "Azerty123!"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Le rôle est obligatoire")
    @Pattern(
            regexp = "ADMIN|OWNER|GUEST",
            message = "Le rôle doit être ADMIN, OWNER ou GUEST"
    )
    @NotBlank(message = "Role is required")
    private String role; // si tu veux le choisir, sinon impose OWNER
}

