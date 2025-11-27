package com.ibrahim.dubaiconciergerie.demo.controller;

import com.ibrahim.dubaiconciergerie.demo.dto.PropertyDto;
import com.ibrahim.dubaiconciergerie.demo.dto.PropertyMapper;
import com.ibrahim.dubaiconciergerie.demo.dto.PropertyResponseDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.entity.User;
import com.ibrahim.dubaiconciergerie.demo.service.PropertyService;
import com.ibrahim.dubaiconciergerie.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/properties")
@CrossOrigin
public class OwnerPropertyController {

    private final PropertyService propertyService;
    private final UserService userService;

    public OwnerPropertyController(PropertyService propertyService,
                                   UserService userService) {
        this.propertyService = propertyService;
        this.userService = userService;
    }

    @GetMapping("/{ownerId}")
    @Operation(summary = "Liste les propriétés d'un propriétaire")
    public List<PropertyResponseDto> getOwnerProperties(@PathVariable Long ownerId) {
        User owner = userService.getById(ownerId);
        return propertyService.getPropertiesForOwner(owner)
                .stream()
                .map(PropertyMapper::toDto)
                .toList();
    }

    @PostMapping("/{ownerId}")
    @Operation(summary = "Créer une propriété pour un propriétaire")
    public PropertyResponseDto createOwnerProperty(
            @PathVariable Long ownerId,
            @Valid @RequestBody PropertyDto dto) {

        User owner = userService.getById(ownerId);

        Property property = Property.builder()
                .title(dto.getTitle())
                .city(dto.getCity())
                .address(dto.getAddress())
                .capacity(dto.getCapacity())
                .rentalType(Property.RentalType.valueOf(dto.getRentalType()))
                .nightlyPrice(dto.getNightlyPrice())
                .monthlyPrice(dto.getMonthlyPrice())
                .owner(owner)
                .build();

        Property saved = propertyService.createProperty(property);

        return PropertyMapper.toDto(saved);
    }
}
