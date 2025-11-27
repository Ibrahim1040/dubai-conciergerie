package com.ibrahim.dubaiconciergerie.demo.repository;

import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByProperty(Property property);

    List<Booking> findByGuestEmail(String guestEmail);

    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    WHERE b.property = :property
      AND b.status <> :canceled
      AND (
            (:startDate BETWEEN b.startDate AND b.endDate)
            OR (:endDate BETWEEN b.startDate AND b.endDate)
            OR (b.startDate BETWEEN :startDate AND :endDate)
          )
    """)
    long countActiveBookingsInRange(@Param("property") Property property,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("canceled") Booking.Status canceled);

}
