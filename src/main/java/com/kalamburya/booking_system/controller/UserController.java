package com.kalamburya.booking_system.controller;

import com.kalamburya.booking_system.dto.UserRequest;
import com.kalamburya.booking_system.dto.UserResponse;
import com.kalamburya.booking_system.entity.User;
import com.kalamburya.booking_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService service) {
        userService = service;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {

        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
        );

        User createdUser = userService.createUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserResponse.of(createdUser));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users =  userService
                .getAllUsers()
                .stream()
                .map(UserResponse::of)
                .toList();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(UserResponse.of(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {

        User updatedData = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
        );

        User updatedUser = userService.updateUser(id, updatedData);

        return ResponseEntity.ok(UserResponse.of(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }




}
