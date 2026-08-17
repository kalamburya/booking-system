package com.kalamburya.booking_system.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType type;

    @Column(name = "room_price_per_night", nullable = false)
    private BigDecimal price;

    @Column(name = "room_capacity", nullable = false)
    private int capacity;

    @Column(name = "room_description")
    private String description;

    public Room(String number, RoomType type, BigDecimal price, int capacity, String description) {
        this.number = number;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.description = description;
    }

    public Room() {}

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
