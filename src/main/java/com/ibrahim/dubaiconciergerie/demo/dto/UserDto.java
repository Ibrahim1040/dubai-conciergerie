package com.ibrahim.dubaiconciergerie.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Schema(
            description = "ID généré par la base",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "1"
    )
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 80)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 80)
    private String lastName;

    @NotBlank(message = "Email ne peut pas être vide")
    @Email(message = "Email doit être valide")
    @Size(max = 150)
    private String email;

    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    @Schema(
            description = "Mot de passe (utilisé uniquement pour la création ou la mise à jour)",
            accessMode = Schema.AccessMode.WRITE_ONLY,
            example = "Azerty123!"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "Role is required")
    private String role;
}
