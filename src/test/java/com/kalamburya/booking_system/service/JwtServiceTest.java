package com.kalamburya.booking_system.service;

import com.kalamburya.booking_system.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_thenExtractUsername_shouldReturnSameEmail() {
        User user = new User("Ivan", "Petrov", "ivan@mail.com", "password");

        String token = jwtService.generateToken(user);
        String extractedEmail = jwtService.extractUsername(token);

        assertEquals("ivan@mail.com", extractedEmail);
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenBelongsToDifferentUser() {
        User user = new User("Ivan", "Petrov", "ivan@mail.com", "password");
        User anotherUser = new User("Petr", "Sidorov", "petr@mail.com", "password");

        String token = jwtService.generateToken(user);

        assertFalse(jwtService.isTokenValid(token, anotherUser));
    }

    @Test
    void isTokenValid_shouldReturnTrue_forCorrectUserAndFreshToken() {
        User user = new User("Ivan", "Petrov", "ivan@mail.com", "password");

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token, user));
    }
}