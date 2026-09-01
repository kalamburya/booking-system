package com.kalamburya.booking_system.service;

import com.kalamburya.booking_system.entity.*;
import com.kalamburya.booking_system.exception.BookingNotFoundException;
import com.kalamburya.booking_system.exception.RoomNotAvailableException;
import com.kalamburya.booking_system.repository.BookingRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository repository;
    private final UserService userService;
    private final RoomService roomService;

    public BookingService(BookingRepository repository,
                          UserService userRepository,
                          RoomService roomRepository) {
        this.repository = repository;
        this.userService = userRepository;
        this.roomService = roomRepository;
    }

    public Booking createBooking(Long userId,
                                 Long roomId,
                                 LocalDate checkIn,
                                 LocalDate checkOut) {

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date isn't after check-in date");
        }

        User user = userService.getUserById(userId);
        Room room = roomService.getRoomById(roomId);

        List<Booking> bookings = repository.findOverlappingBookings(roomId, checkIn, checkOut);

        if (!bookings.isEmpty()) {
            throw new RoomNotAvailableException(roomId, checkIn, checkOut);
        }

        long daysBetweenCheckInAndCheckOut = ChronoUnit.DAYS.between(checkIn,checkOut);
        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(daysBetweenCheckInAndCheckOut));

        return repository.save(new Booking(user, room, checkIn, checkOut, totalPrice));
    }

    public List<Booking> getUserBookings(Long userId) {

        userService.getUserById(userId);

        return repository.findByUserId(userId);
    }

    public void cancelBooking(Long bookingId, User currentUser) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        boolean isOwner = booking.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You can cancel only your own bookings");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        repository.save(booking);
    }
}
