package com.kalamburya.booking_system.service;

import com.kalamburya.booking_system.entity.Booking;
import com.kalamburya.booking_system.entity.BookingStatus;
import com.kalamburya.booking_system.entity.Room;
import com.kalamburya.booking_system.entity.RoomType;
import com.kalamburya.booking_system.entity.User;
import com.kalamburya.booking_system.exception.RoomNotAvailableException;
import com.kalamburya.booking_system.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Room room;

    @BeforeEach
    void setUp() {
        user = new User("Ivan", "Petrov", "ivan@mail.com", "password");
        user.setId(1L);

        room = new Room("101", RoomType.SINGLE, BigDecimal.valueOf(100), 2, "desc");
        room.setId(1L);
    }

    @Test
    void createBooking_shouldThrow_whenCheckOutIsBeforeCheckIn() {

        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(2);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(1L, 1L, checkIn, checkOut)
        );

        assertTrue(exception.getMessage().contains("after"));
    }

    @Test
    void createBooking_shouldThrow_whenCheckInIsInThePast() {

        LocalDate checkIn = LocalDate.now().minusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(2);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(1L, 1L, checkIn, checkOut)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("past"));
    }

    @Test
    void createBooking_shouldThrow_whenRoomIsNotAvailable() {

        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(10);

        when(userService.getUserById(1L)).thenReturn(user);
        when(roomService.getRoomById(1L)).thenReturn(room);
        when(bookingRepository.findOverlappingBookings(1L, checkIn, checkOut))
                .thenReturn(List.of(new Booking(user, room, checkIn, checkOut, BigDecimal.TEN)));

        assertThrows(
                RoomNotAvailableException.class,
                () -> bookingService.createBooking(1L, 1L, checkIn, checkOut)
        );
    }

    @Test
    void createBooking_shouldCalculateTotalPriceCorrectly() {

        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(10); // 5 ночей

        when(userService.getUserById(1L)).thenReturn(user);
        when(roomService.getRoomById(1L)).thenReturn(room);
        when(bookingRepository.findOverlappingBookings(1L, checkIn, checkOut))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.createBooking(1L, 1L, checkIn, checkOut);

        assertEquals(0, BigDecimal.valueOf(500).compareTo(result.getTotalPrice()));
        assertEquals(BookingStatus.PENDING, result.getStatus());
    }

    @Test
    void cancelBooking_shouldSucceed_whenUserIsOwner() {

        Booking booking = new Booking(
                user,
                room,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                BigDecimal.TEN
        );

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L, user);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void cancelBooking_shouldThrow_whenUserIsNotOwnerAndNotAdmin() {

        User otherUser = new User(
                "Petr",
                "Sidorov",
                "petr@mail.com",
                "password");

        otherUser.setId(2L);

        Booking booking = new Booking(
                user,
                room,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                BigDecimal.TEN);

        when(bookingRepository
                .findById(1L))
                .thenReturn(Optional.of(booking));

        assertThrows(
                AccessDeniedException.class,
                () -> bookingService.cancelBooking(1L, otherUser)
        );
    }
}