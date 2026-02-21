package com.ibrahim.dubaiconciergerie.demo.service.impl;

import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.kafka.event.BookingCreatedEvent;
import com.ibrahim.dubaiconciergerie.demo.kafka.producer.BookingEventProducer;
import com.ibrahim.dubaiconciergerie.demo.repository.BookingRepository;
import com.ibrahim.dubaiconciergerie.demo.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    private BookingRepository bookingRepo;
    private PropertyRepository propertyRepo;
    private BookingServiceImpl bookingService;

    private BookingEventProducer producer;
    private ObjectProvider<BookingEventProducer> producerProvider;

    @BeforeEach
    void setUp() {
        bookingRepo = mock(BookingRepository.class);
        propertyRepo = mock(PropertyRepository.class);

        producer = mock(BookingEventProducer.class);
        producerProvider = mock(ObjectProvider.class);

        doAnswer(invocation -> {
            var consumer = invocation.getArgument(0, java.util.function.Consumer.class);
            consumer.accept(producer);
            return null;
        }).when(producerProvider).ifAvailable(any());


        bookingService = new BookingServiceImpl(bookingRepo, propertyRepo, producerProvider);
    }

    @Test
    void shouldCreateBookingSuccessfully() {
        // GIVEN
        BookingDto dto = BookingDto.builder()
                .propertyId(1L)
                .guestName("Ibrahim")
                .guestEmail("ibrahim@test.com")
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(5))
                .totalPrice(300.0)
                .status("PENDING")
                .build();

        Property property = new Property();
        property.setId(1L);

        when(propertyRepo.findById(1L)).thenReturn(Optional.of(property));

        when(bookingRepo.countActiveBookingsInRange(
                eq(property),
                eq(dto.getStartDate()),
                eq(dto.getEndDate()),
                eq(Booking.Status.CANCELED)
        )).thenReturn(0L);

        Booking saved = Booking.builder()
                .id(10L)
                .property(property)
                .guestName("Ibrahim")
                .guestEmail("ibrahim@test.com")
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalPrice(dto.getTotalPrice())
                .status(Booking.Status.PENDING)
                .build();

        when(bookingRepo.save(any(Booking.class))).thenReturn(saved);

        // WHEN
        Booking result = bookingService.create(dto);

        // THEN
        assertNotNull(result);
        assertEquals("Ibrahim", result.getGuestName());
        assertEquals(Booking.Status.PENDING, result.getStatus());

        verify(bookingRepo).save(any(Booking.class));
        verify(producer).send(any(BookingCreatedEvent.class)); // ✅ event envoyé
    }

    @Test
    void shouldThrowErrorWhenPropertyNotFound() {
        BookingDto dto = BookingDto.builder()
                .propertyId(99L)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2))
                .build();

        when(propertyRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.create(dto));
        verifyNoInteractions(bookingRepo);
    }

    @Test
    void shouldThrowErrorWhenBookingInPast() {
        Property property = new Property();
        property.setId(1L);

        when(propertyRepo.findById(1L)).thenReturn(Optional.of(property));

        BookingDto dto = BookingDto.builder()
                .propertyId(1L)
                .startDate(LocalDate.now().minusDays(2))
                .endDate(LocalDate.now().plusDays(1))
                .build();

        assertThrows(ResponseStatusException.class, () -> bookingService.create(dto));
        verify(bookingRepo, never()).save(any());
    }

    @Test
    void shouldThrowErrorWhenConflictExists() {
        Property property = new Property();
        property.setId(1L);

        when(propertyRepo.findById(1L)).thenReturn(Optional.of(property));

        BookingDto dto = BookingDto.builder()
                .propertyId(1L)
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(5))
                .build();

        when(bookingRepo.countActiveBookingsInRange(any(), any(), any(), any()))
                .thenReturn(2L);

        assertThrows(ResponseStatusException.class, () -> bookingService.create(dto));
        verify(bookingRepo, never()).save(any());
    }

    @Test
    void shouldCreateBookingEvenIfKafkaDisabled() {
        // GIVEN: producer absent
        doNothing().when(producerProvider).ifAvailable(any());

        BookingDto dto = BookingDto.builder()
                .propertyId(1L)
                .guestName("Ibrahim")
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(5))
                .build();

        Property property = new Property();
        property.setId(1L);

        when(propertyRepo.findById(1L)).thenReturn(Optional.of(property));
        when(bookingRepo.countActiveBookingsInRange(any(), any(), any(), any())).thenReturn(0L);

        Booking saved = Booking.builder().id(10L).property(property).guestName("Ibrahim").status(Booking.Status.PENDING).build();
        when(bookingRepo.save(any(Booking.class))).thenReturn(saved);

        // WHEN
        Booking result = bookingService.create(dto);

        // THEN
        assertNotNull(result);
        verify(bookingRepo).save(any(Booking.class));
        verifyNoInteractions(producer); // ✅ aucun send
    }
}
