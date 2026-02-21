package com.ibrahim.dubaiconciergerie.demo.kafka.event;

public record BookingCreatedEvent(Long bookingId, Long propertyId, String guestName) {}
