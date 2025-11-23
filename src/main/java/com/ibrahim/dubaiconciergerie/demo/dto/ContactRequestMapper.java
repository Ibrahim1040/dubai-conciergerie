package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.ContactRequest;

import java.time.LocalDateTime;

public final class ContactRequestMapper {

    private ContactRequestMapper() {
    }

    public static ContactRequestDto toDto(ContactRequest entity) {
        if (entity == null) {
            return null;
        }

        return ContactRequestDto.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .propertyType(entity.getPropertyType())
                .rentalType(entity.getRentalType())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static ContactRequest fromDto(ContactRequestDto dto) {
        if (dto == null) return null;

        ContactRequest cr = new ContactRequest();
        cr.setFullName(dto.getFullName());
        cr.setEmail(dto.getEmail());
        cr.setPropertyType(dto.getPropertyType());
        cr.setRentalType(dto.getRentalType());
        cr.setMessage(dto.getMessage());
        cr.setStatus(dto.getStatus()); // String
        return cr;
    }

    public static ContactRequest toEntity(ContactRequestDto dto) {
        if (dto == null) {
            return null;
        }

        ContactRequest contact = new ContactRequest();
        contact.setId(dto.getId());
        contact.setFullName(dto.getFullName());
        contact.setEmail(dto.getEmail());
        contact.setPropertyType(dto.getPropertyType());
        contact.setRentalType(dto.getRentalType());
        contact.setMessage(dto.getMessage());
        // createdAt sera mis dans le controller/service
        return contact;
    }

    public static ContactRequestResponseDto toResponseDto(ContactRequest cr) {
        if (cr == null) return null;

        return ContactRequestResponseDto.builder()
                .id(cr.getId())
                .fullName(cr.getFullName())
                .email(cr.getEmail())
                .propertyType(cr.getPropertyType())
                .rentalType(cr.getRentalType())
                .message(cr.getMessage())
                .status(cr.getStatus())
                .createdAt(cr.getCreatedAt())
                .build();
    }
}
