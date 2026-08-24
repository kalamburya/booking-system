package com.kalamburya.booking_system.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Long id) {
        super("Booking not found by id: " + id);
    }
}
