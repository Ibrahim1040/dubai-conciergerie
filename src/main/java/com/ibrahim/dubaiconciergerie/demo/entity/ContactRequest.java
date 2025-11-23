package com.ibrahim.dubaiconciergerie.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 100)
    private String propertyType;

    @Column(length = 50)
    private String rentalType;

    @Column(length = 2000, nullable = false)
    private String message;

    @Column(length = 2000, nullable = false)
    private String status;

    private LocalDateTime createdAt;
}
