package com.ibrahim.dubaiconciergerie.demo.repository;

import com.ibrahim.dubaiconciergerie.demo.entity.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {
}

