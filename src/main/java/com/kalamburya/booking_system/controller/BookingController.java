package com.kalamburya.booking_system.controller;

import com.kalamburya.booking_system.dto.BookingRequest;
import com.kalamburya.booking_system.dto.BookingResponse;
import com.kalamburya.booking_system.entity.Booking;
import com.kalamburya.booking_system.entity.User;
import com.kalamburya.booking_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal User currentUser) {

        Booking booking = bookingService.createBooking(
                currentUser.getId(),
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(BookingResponse.of(booking));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@AuthenticationPrincipal User currentUser) {

        List<BookingResponse> bookings = userBookingsToBookingResponseList(currentUser.getId());

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingResponse>> getUserBookings(@PathVariable Long userId) {

        List<BookingResponse> bookings = userBookingsToBookingResponseList(userId);

        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {

        bookingService.cancelBooking(id, currentUser);

        return ResponseEntity.noContent().build();
    }

    private List<BookingResponse> userBookingsToBookingResponseList(Long userId) {
        return bookingService.getUserBookings(userId).stream()
                .map(BookingResponse::of)
                .toList();
    }
}
