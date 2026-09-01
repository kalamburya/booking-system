package com.kalamburya.booking_system.repository;

import com.kalamburya.booking_system.entity.Booking;
import com.kalamburya.booking_system.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.room " +
            "JOIN FETCH b.user " +
            "WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b " +
            "where b.room.id = :roomId " +
            "AND b.status <> 'CANCELLED' " +
            "AND b.checkIn < :checkOut " +
            "AND b.checkOut > :checkIn " )
    List<Booking> findOverlappingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
            );

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.user.id = :userId AND b.status <> 'CANCELLED'")
    boolean existsActiveBookingForUser(@Param("userId") Long userId);
}
