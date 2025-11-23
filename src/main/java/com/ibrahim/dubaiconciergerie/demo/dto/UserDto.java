package com.ibrahim.dubaiconciergerie.demo.dto;


import com.ibrahim.dubaiconciergerie.demo.entity.User;
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

    @NotBlank
    @Size(max = 80)
    private String firstName;

    @NotBlank
    @Size(max = 80)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    @Schema(
            description = "Mot de passe (seulement à la création / mise à jour)",
            accessMode = Schema.AccessMode.WRITE_ONLY,
            example = "Azerty123!"
    )
    private String password;

    private String role; // si tu veux le choisir, sinon impose OWNER
}

