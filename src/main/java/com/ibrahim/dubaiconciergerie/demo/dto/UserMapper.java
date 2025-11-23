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
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        if (dto.getRole() != null) {
            try {
                user.setRole(User.Role.valueOf(dto.getRole().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                // on relance avec un message clair -> catché par le handler ci-dessous
                throw new IllegalArgumentException("Rôle invalide : " + dto.getRole());
            }
        } else {
            user.setRole(null);
        }

        return user;
    }
}
