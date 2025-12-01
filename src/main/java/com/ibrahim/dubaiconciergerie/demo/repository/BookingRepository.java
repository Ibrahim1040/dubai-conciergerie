package com.ibrahim.dubaiconciergerie.demo.repository;

import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    WHERE b.property = :property
      AND b.status <> :excludedStatus
      AND b.startDate < :endDate
      AND b.endDate > :startDate
    """)
    long countActiveBookingsInRange(
            @Param("property") Property property,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("excludedStatus") Booking.Status excludedStatus
    );



    List<Booking> findByProperty(Property property);

    List<Booking> findByGuestEmail(String guestEmail);

    List<Booking> findByPropertyIdOrderByStartDateAsc(Long propertyId);

    // 👉 Tous les bookings pour une propriété
    List<Booking> findByPropertyId(Long propertyId);

    // 👉 Tous les bookings pour un owner (via ses propriétés)
    List<Booking> findByPropertyOwnerId(Long ownerId);
}

