package com.kalamburya.booking_system.dto;

import com.kalamburya.booking_system.entity.Room;
import com.kalamburya.booking_system.entity.RoomType;

import java.math.BigDecimal;

public class RoomResponse {

    private Long id;

    private String number;

    private RoomType type;

    private BigDecimal price;

    private int capacity;

    private String description;

    public RoomResponse() {
    }

    public static RoomResponse of(Room room) {
        RoomResponse response = new RoomResponse();
        response.id = room.getId();
        response.number = room.getNumber();
        response.type = room.getType();
        response.price = room.getPrice();
        response.capacity = room.getCapacity();
        response.description = room.getDescription();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public RoomType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }
}
