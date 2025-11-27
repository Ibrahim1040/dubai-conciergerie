package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.entity.User.Role;

public class UserMapper {

    // ===== ENTITE → DTO =====
    public static UserDto toDto(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                // On convertit enum → String
                .role(user.getRole().name())
                .build();
    }

    // ===== DTO → ENTITE =====
    public static User fromDto(UserDto dto) {
        if (dto == null) return null;

        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword()) // sera encodé dans le service
                // On convertit String → enum
                .role(Role.valueOf(dto.getRole()))
                .build();
    }

    // ===== ENTITE → DTO résumé pour Property =====
    public static UserSummaryDto toSummaryDto(User user) {
        if (user == null) return null;

        return UserSummaryDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }

    // ---------------- TO ENTITY ----------------
    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword()) // sera encodé plus tard dans le service
                .role(User.Role.valueOf(dto.getRole()))
                .build();
    }
}
