package com.ibrahim.dubaiconciergerie.demo.controller;

import com.ibrahim.dubaiconciergerie.demo.dto.ContactRequestDto;
import com.ibrahim.dubaiconciergerie.demo.dto.ContactRequestMapper;
import com.ibrahim.dubaiconciergerie.demo.dto.ContactRequestResponseDto;
import com.ibrahim.dubaiconciergerie.demo.entity.ContactRequest;
import com.ibrahim.dubaiconciergerie.demo.service.ContactRequestService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/contact-requests")
@CrossOrigin
public class ContactRequestController {

    private final ContactRequestService contactRequestService;

    public ContactRequestController(ContactRequestService contactRequestService) {
        this.contactRequestService = contactRequestService;
    }

    // 1) Liste de toutes les demandes de contact (back-office)
    @GetMapping
    @Operation(summary = "Lister toutes les demandes de contact")
    public List<ContactRequestResponseDto> getAll() {
        List<ContactRequest> list = contactRequestService.getAll();
        return list.stream()
                .map(ContactRequestMapper::toResponseDto)
                .toList();
    }

    // 2) Récupérer une demande par id
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une demande de contact par id")
    public ContactRequestResponseDto getById(@PathVariable Long id) {
        ContactRequest cr = contactRequestService.getById(id);
        return ContactRequestMapper.toResponseDto(cr);
    }

    // 3) Créer une demande de contact manuellement (back-office)
    @PostMapping
    @Operation(summary = "Créer une demande de contact (back-office)")
    public ContactRequestResponseDto create(@Valid @RequestBody ContactRequestDto dto) {
        ContactRequest saved = contactRequestService.create(dto);
        return ContactRequestMapper.toResponseDto(saved);
    }

    // 4) Changer le statut (NEW / IN_PROGRESS / CLOSED, etc.)
    @PatchMapping("/{id}/status")
    @Operation(summary = "Mettre à jour le statut d'une demande de contact")
    public ContactRequestResponseDto updateStatus(@PathVariable Long id,
                                                  @RequestParam String status) {
        ContactRequest updated = contactRequestService.updateStatus(id, status);
        return ContactRequestMapper.toResponseDto(updated);
    }

    // 5) Supprimer une demande de contact
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une demande de contact")
    public void delete(@PathVariable Long id) {
        contactRequestService.delete(id);
    }
}
