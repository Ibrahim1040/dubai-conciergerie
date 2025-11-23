package com.ibrahim.dubaiconciergerie.demo.repository;

import com.ibrahim.dubaiconciergerie.demo.entity.Booking;
import com.ibrahim.dubaiconciergerie.demo.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Déjà existant
    List<Booking> findByProperty(Property property);

    // ✅ NOUVELLE VERSION : on passe le statut à exclure en paramètre
    @Query("""
           SELECT COUNT(b) FROM Booking b
           WHERE b.property = :property
             AND b.status <> :excludedStatus
             AND b.endDate >= :startDate
             AND b.startDate <= :endDate
           """)
    long countActiveBookingsInRange(@Param("property") Property property,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("excludedStatus") Booking.Status excludedStatus);
}
