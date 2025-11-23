package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.entity.User.Role;

public final class UserMapper {

    private UserMapper() {}

    // DTO complet (back-office)
    public static UserDto toDto(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword()) // ou null si tu ne veux pas l’exposer
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }

    // DTO résumé (utilisé dans PropertyResponseDto par ex.)
    public static UserSummaryDto toSummaryDto(User user) {
        if (user == null) return null;

        return UserSummaryDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }

    public static User fromDto(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        if (dto.getRole() != null) {
            String roleStr = dto.getRole().toUpperCase();
            try {
                user.setRole(Role.valueOf(roleStr));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid role: " + dto.getRole());
            }
        }

        return user;
    }
}
