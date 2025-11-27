package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;

public class PropertyMapper {

    // ENTITY → DTO (lecture)
    public static PropertyResponseDto toDto(Property property) {
        return PropertyResponseDto.builder()
                .id(property.getId())
                .title(property.getTitle())
                .city(property.getCity())
                .address(property.getAddress())
                .capacity(property.getCapacity())
                .rentalType(property.getRentalType().name())
                .nightlyPrice(property.getNightlyPrice())
                .monthlyPrice(property.getMonthlyPrice())
                .ownerId(property.getOwner().getId())
                .build();
    }

    // DTO → ENTITY (création + update)
    public static Property toEntity(PropertyDto dto) {
        if (dto == null) return null;

        Property property = new Property();
        property.setTitle(dto.getTitle());
        property.setCity(dto.getCity());
        property.setAddress(dto.getAddress());
        property.setCapacity(dto.getCapacity());

        try {
            property.setRentalType(Property.RentalType.valueOf(dto.getRentalType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid rental type: " + dto.getRentalType());
        }


        property.setNightlyPrice(dto.getNightlyPrice());
        property.setMonthlyPrice(dto.getMonthlyPrice());

        return property;
    }
}
