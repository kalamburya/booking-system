package com.kalamburya.booking_system.exception;

import java.time.LocalDate;

public class RoomNotAvailableException extends RuntimeException {

    public RoomNotAvailableException(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        super("Room with id " + roomId + " is not available from " + checkIn + " to " + checkOut);
    }
}