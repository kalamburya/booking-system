package com.kalamburya.booking_system.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long id){
        super("Room not found by id: " + id);
    }
}
