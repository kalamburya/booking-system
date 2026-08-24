package com.kalamburya.booking_system.dto;

import com.kalamburya.booking_system.entity.Booking;
import com.kalamburya.booking_system.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;

    private String roomNumber;

    private String userEmail;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private BookingStatus status;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    public BookingResponse() {
    }

    public BookingResponse(Long id,
                           String roomNumber,
                           String userEmail,
                           LocalDate checkIn,
                           LocalDate checkOut,
                           BookingStatus status,
                           BigDecimal totalPrice,
                           LocalDateTime createdAt) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.userEmail = userEmail;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public static BookingResponse of(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getRoom().getNumber(),
                booking.getUser().getEmail(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getStatus(),
                booking.getTotalPrice(),
                booking.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
