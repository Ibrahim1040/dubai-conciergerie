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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/properties")
@CrossOrigin
public class PropertyController {

    private final PropertyService propertyService;
    private final UserService userService;

    public PropertyController(PropertyService propertyService,
                              UserService userService) {
        this.propertyService = propertyService;
        this.userService = userService;
    }

    // ---------------------------------------------------------------------
    // LISTE DES LOGEMENTS D’UN PROPRIÉTAIRE
    // ---------------------------------------------------------------------
    @GetMapping("/{ownerId}")
    @Operation(summary = "Liste les propriétés d'un propriétaire")
    public List<PropertyResponseDto> getOwnerProperties(@PathVariable Long ownerId) {
        User owner = userService.getById(ownerId);

        return propertyService.getPropertiesForOwner(owner)
                .stream()
                .map(PropertyMapper::toDto)
                .toList();
    }

    // ---------------------------------------------------------------------
    // CRÉATION D’UN LOGEMENT
    // ---------------------------------------------------------------------
    @PostMapping("/{ownerId}")
    @Operation(summary = "Créer une propriété pour un propriétaire")
    public PropertyResponseDto createOwnerProperty(@PathVariable Long ownerId,
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

    // ---------------------------------------------------------------------
    // RÉCUPÉRATION D’UN LOGEMENT PARTICULIER
    // ---------------------------------------------------------------------
    @GetMapping("/{ownerId}/{propertyId}")
    @Operation(summary = "Récupère une propriété précise d'un propriétaire")
    public PropertyResponseDto getOwnerProperty(@PathVariable Long ownerId,
                                                @PathVariable Long propertyId) {

        Property property = propertyService.getOwnerProperty(ownerId, propertyId);
        return PropertyMapper.toDto(property);
    }

    // ---------------------------------------------------------------------
    // MISE À JOUR D’UN LOGEMENT
    // ---------------------------------------------------------------------
    @PutMapping("/{ownerId}/{propertyId}")
    @Operation(summary = "Met à jour une propriété d'un propriétaire")
    public PropertyResponseDto updateOwnerProperty(@PathVariable Long ownerId,
                                                   @PathVariable Long propertyId,
                                                   @Valid @RequestBody PropertyDto dto) {

        User owner = userService.getById(ownerId);

        // on construit un objet "updates" avec les nouvelles valeurs
        Property updates = Property.builder()
                .title(dto.getTitle())
                .city(dto.getCity())
                .address(dto.getAddress())
                .capacity(dto.getCapacity())
                .rentalType(Property.RentalType.valueOf(dto.getRentalType()))
                .nightlyPrice(dto.getNightlyPrice())
                .monthlyPrice(dto.getMonthlyPrice())
                .owner(owner)
                .build();

        // ⚠️ ici on appelle le service, PAS la méthode getOwnerProperty du controller
        Property updated = propertyService.updateOwnerProperty(ownerId, propertyId, updates);

        return PropertyMapper.toDto(updated);
    }

    // ---------------------------------------------------------------------
    // SUPPRESSION D’UN LOGEMENT
    // ---------------------------------------------------------------------
    @DeleteMapping("/{ownerId}/{propertyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprime une propriété d'un propriétaire")
    public void deleteOwnerProperty(@PathVariable Long ownerId,
                                    @PathVariable Long propertyId) {

        propertyService.deleteOwnerProperty(ownerId, propertyId);
    }
}
