package com.kalamburya.booking_system.controller;

import com.kalamburya.booking_system.dto.BookingRequest;
import com.kalamburya.booking_system.dto.BookingResponse;
import com.kalamburya.booking_system.entity.Booking;
import com.kalamburya.booking_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(
                request.getUserId(),
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(BookingResponse.of(booking));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getUserBookings(@PathVariable Long userId) {
        List<BookingResponse> bookings = bookingService.getUserBookings(userId).stream()
                .map(BookingResponse::of)
                .toList();
        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}
