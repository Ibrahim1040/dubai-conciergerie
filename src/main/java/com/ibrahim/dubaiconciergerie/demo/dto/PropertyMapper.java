package com.ibrahim.dubaiconciergerie.demo.dto;

import com.ibrahim.dubaiconciergerie.demo.entity.Property;

public final class PropertyMapper {

    private PropertyMapper() {
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
                .rentalType(property.getRentalType())
                .nightlyPrice(property.getNightlyPrice())
                .monthlyPrice(property.getMonthlyPrice())
                .owner(UserMapper.toSummaryDto(property.getOwner()))
                .build();
    }
}
