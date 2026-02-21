package com.ibrahim.dubaiconciergerie.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibrahim.dubaiconciergerie.demo.dto.BookingDto;
import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import com.ibrahim.dubaiconciergerie.demo.service.BookingService;
import com.ibrahim.dubaiconciergerie.demo.service.PropertyService;
import com.ibrahim.dubaiconciergerie.demo.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.eq;


import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    void shouldReturn200() throws Exception {
        when(bookingService.getAll(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/owner/bookings?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }


    @Test
    void shouldCreateBooking() throws Exception {
        BookingDto dto = BookingDto.builder()
                .propertyId(1L)
                .guestName("Ibrahim")
                .guestEmail("test@test.com")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(3))
                .totalPrice(200.0)
                .status("PENDING")
                .build();

        Property property = new Property();
        property.setId(1L);


        Booking saved = Booking.builder()
                .id(10L)
                .property(property)
                .guestName("Ibrahim")
                .guestEmail("test@test.com")
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalPrice(dto.getTotalPrice())
                .status(Booking.Status.PENDING)
                .build();

        when(bookingService.create(any(BookingDto.class))).thenReturn(saved);

        mockMvc.perform(post("/api/owner/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // ⚠️ si tu as modifié le controller => 201
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.guestName").value("Ibrahim"))
                .andExpect(jsonPath("$.propertyId").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldReturn400_whenCreateBookingInvalid() throws Exception {
        // manque propertyId + guestEmail vide => validation doit renvoyer 400
        String invalidJson = """
                {
                  "guestName": "Ibrahim",
                  "guestEmail": "",
                  "startDate": "%s",
                  "endDate": "%s",
                  "totalPrice": 200.0,
                  "status": "PENDING"
                }
                """.formatted(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));

        mockMvc.perform(post("/api/owner/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBookingById() throws Exception {
        BookingDto dto = BookingDto.builder()
                .id(10L)
                .propertyId(1L)
                .guestName("Ibrahim")
                .status("CONFIRMED")
                .build();

        when(bookingService.getById(10L)).thenReturn(dto);

        mockMvc.perform(get("/api/owner/bookings/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guestName").value("Ibrahim"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldDeleteBooking() throws Exception {
        doNothing().when(bookingService).cancel(5L);

        mockMvc.perform(delete("/api/owner/bookings/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetBookingsForProperty() throws Exception {
        Property property = new Property();
        property.setId(1L);

        Booking booking = new Booking();
        booking.setId(20L);
        booking.setProperty(property);
        booking.setGuestName("Test");
        booking.setGuestEmail("test@test.com");
        booking.setStartDate(LocalDate.now().plusDays(1));
        booking.setEndDate(LocalDate.now().plusDays(2));
        booking.setStatus(Booking.Status.PENDING);
        booking.setTotalPrice(100.0);


        when(propertyService.getById(1L)).thenReturn(property);
        when(bookingService.getByProperty(property)).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/owner/bookings/property/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(20L));
    }

    @Test
    void shouldGetBookingsForOwner() throws Exception {
        Property property = new Property();
        property.setId(1L);

        Booking booking = new Booking();
        booking.setId(20L);
        booking.setProperty(property);
        booking.setGuestName("Test");
        booking.setGuestEmail("test@test.com");
        booking.setStartDate(LocalDate.now().plusDays(1));
        booking.setEndDate(LocalDate.now().plusDays(2));
        booking.setTotalPrice(100.0);
        booking.setStatus(Booking.Status.PENDING);

        when(bookingService.getByOwner(7L)).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/owner/bookings/owner/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(20L))
                .andExpect(jsonPath("$[0].propertyId").value(1L));
    }

    @Test
    void shouldReturnPagedBookings() throws Exception {
        Property property = new Property();
        property.setId(1L);

        Booking booking = new Booking();
        booking.setId(20L);
        booking.setProperty(property);
        booking.setGuestName("Test");
        booking.setGuestEmail("test@test.com");
        booking.setStartDate(LocalDate.now().plusDays(1));
        booking.setEndDate(LocalDate.now().plusDays(2));
        booking.setTotalPrice(100.0);
        booking.setStatus(Booking.Status.PENDING);

        Page<Booking> page = new PageImpl<>(
                List.of(booking),
                PageRequest.of(0, 20),
                1
        );

        when(bookingService.getAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/owner/bookings?page=0&size=20&sort=startDate,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(20L))
                .andExpect(jsonPath("$.content[0].propertyId").value(1L));
    }

    @Test
    void shouldGetPagedBookingsForProperty() throws Exception {
        Property property = new Property();
        property.setId(1L);

        Booking booking = new Booking();
        booking.setId(20L);
        booking.setProperty(property);

        Page<Booking> page = new PageImpl<>(List.of(booking), PageRequest.of(0, 20), 1);

        when(bookingService.getByProperty(eq(1L), any())).thenReturn(page);

        mockMvc.perform(get("/api/owner/bookings/property/1?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(20L))
                .andExpect(jsonPath("$.content[0].propertyId").value(1L));
    }

    @Test
    void shouldGetPagedBookingsForOwner() throws Exception {
        Property property = new Property();
        property.setId(1L);

        Booking booking = new Booking();
        booking.setId(30L);
        booking.setProperty(property);

        Page<Booking> page = new PageImpl<>(List.of(booking), PageRequest.of(0, 20), 1);

        when(bookingService.getByOwner(eq(7L), any())).thenReturn(page);

        mockMvc.perform(get("/api/owner/bookings/owner/7?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(30L));
    }

}
