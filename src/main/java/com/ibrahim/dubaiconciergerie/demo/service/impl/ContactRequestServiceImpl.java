package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.dto.ContactRequestDto;
import com.ibrahim.dubaiconciergerie.demo.dto.ContactRequestMapper;
import com.ibrahim.dubaiconciergerie.demo.entity.ContactRequest;
import com.ibrahim.dubaiconciergerie.demo.exception.ResourceNotFoundException;
import com.ibrahim.dubaiconciergerie.demo.repository.ContactRequestRepository;
import com.ibrahim.dubaiconciergerie.demo.service.ContactRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactRequestServiceImpl implements ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;

    public ContactRequestServiceImpl(ContactRequestRepository contactRequestRepository) {
        this.contactRequestRepository = contactRequestRepository;
    }

    @Transactional
    public ContactRequest create(ContactRequestDto dto) {
        ContactRequest entity = ContactRequestMapper.fromDto(dto);
        entity.setCreatedAt(LocalDateTime.now());
        if (entity.getStatus() == null) {
            entity.setStatus("NEW");
        }
        return contactRequestRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<ContactRequest> getAll() {
        return contactRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ContactRequest getById(Long id) {
        return contactRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactRequest not found with id " + id));
    }

    @Transactional(readOnly = true)
    public ContactRequest updateStatus(Long id, String status) {
        ContactRequest cr = getById(id);
        cr.setStatus(status);
        return contactRequestRepository.save(cr);
    }

    @Transactional
    public void delete(Long id) {
        ContactRequest cr = getById(id);
        contactRequestRepository.delete(cr);
    }
}
