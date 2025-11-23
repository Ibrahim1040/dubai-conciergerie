package com.ibrahim.dubaiconciergerie.demo.service;

import com.ibrahim.dubaiconciergerie.demo.dto.ContactRequestDto;
import com.ibrahim.dubaiconciergerie.demo.entity.ContactRequest;

import java.util.List;

public interface ContactRequestService {

    ContactRequest create(ContactRequestDto dto);

    List<ContactRequest> getAll();

    ContactRequest getById(Long id);

    ContactRequest updateStatus(Long id, String status);

    void delete(Long id);
}
