package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.dto.PropertyRequestDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;

public class PropertyMapper {

    private PropertyMapper() {
        // utility class
    }

    public static PropertyResponseDto toDto(Property property) {
        if (property == null) {
            return null;
        }

        return PropertyResponseDto.builder()
                .id(property.getId())
                .title(property.getTitle())
                .city(property.getCity())
                .address(property.getAddress())
                .capacity(property.getCapacity())
                .rentalType(
                        property.getRentalType() != null
                                ? property.getRentalType().name()
                                : null
                )
                .nightlyPrice(property.getNightlyPrice())
                .monthlyPrice(property.getMonthlyPrice())
                .build();
    }

    // 🔥 ICI : on prend un PropertyRequestDto, plus PropertyDto
    public static Property toEntity(PropertyRequestDto dto, User owner) {
        if (dto == null) {
            return null;
        }

        return Property.builder()
                .title(dto.getTitle())
                .city(dto.getCity())
                .address(dto.getAddress())
                .capacity(dto.getCapacity())
                .rentalType(Property.RentalType.valueOf(dto.getRentalType()))
                .nightlyPrice(dto.getNightlyPrice())
                .monthlyPrice(dto.getMonthlyPrice())
                .owner(owner)
                .build();
    }

    public static void updateEntity(Property property, PropertyRequestDto dto) {
        property.setTitle(dto.getTitle());
        property.setCity(dto.getCity());
        property.setAddress(dto.getAddress());
        property.setCapacity(dto.getCapacity());
        property.setRentalType(Property.RentalType.valueOf(dto.getRentalType()));
        property.setNightlyPrice(dto.getNightlyPrice());
        property.setMonthlyPrice(dto.getMonthlyPrice());
    }
}
