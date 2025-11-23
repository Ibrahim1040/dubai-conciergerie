package com.ibrahim.dubaiconciergerie.demo.controller;


import com.ibrahim.dubaiconciergerie.demo.dto.*;
import com.ibrahim.dubaiconciergerie.demo.entity.ContactRequest;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.repository.ContactRequestRepository;
import com.ibrahim.dubaiconciergerie.demo.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin // pour ton front React/Angular
public class PublicController {

    private final PropertyService propertyService;
    private final ContactRequestRepository contactRequestRepository;

    public PublicController(PropertyService propertyService,
                            ContactRequestRepository contactRequestRepository) {
        this.propertyService = propertyService;
        this.contactRequestRepository = contactRequestRepository;
    }

    @GetMapping("/properties")
    @Operation(summary = "Liste les propriétés publiques")
    public List<PropertyResponseDto> getPublicProperties() {
        return propertyService.getAllPublicProperties()
                .stream()
                .map(PropertyMapper::toDto)
                .toList();
    }


    @GetMapping("/contact-requests")
    public List<ContactRequestDto> getAllContactRequests() {
        return contactRequestRepository.findAll().stream()
                .map(ContactRequestMapper::toDto)
                .toList();
    }


    @PostMapping("/contact")
    @Operation(summary = "Créer une demande de contact")
    public ContactRequestDto createContactRequest(@Valid @RequestBody ContactRequestDto dto) {
        ContactRequest entity = ContactRequestMapper.fromDto(dto);
        entity.setCreatedAt(LocalDateTime.now());
        ContactRequest saved = contactRequestRepository.save(entity);
        return ContactRequestMapper.toDto(saved);
    }

    @PostMapping("/properties")
    @Operation(summary = "Créer une propriété (demo / test)")
    public Property createProperty(@Valid @RequestBody PropertyDto dto) {

        Property property = new Property();
        property.setTitle(dto.getTitle());
        property.setCity(dto.getCity());
        property.setAddress(dto.getAddress());
        property.setCapacity(dto.getCapacity());

        // conversion de String vers enum
        property.setRentalType(dto.getRentalType());

        property.setNightlyPrice(dto.getNightlyPrice());
        property.setMonthlyPrice(dto.getMonthlyPrice());

        property.setOwner(null);

        return propertyService.createProperty(property);
    }

}

