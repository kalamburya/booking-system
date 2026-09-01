package com.kalamburya.booking_system.controller;

import com.kalamburya.booking_system.dto.RoomRequest;
import com.kalamburya.booking_system.dto.RoomResponse;
import com.kalamburya.booking_system.entity.Room;
import com.kalamburya.booking_system.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {

        Room room = new Room(
                request.getNumber(),
                request.getType(),
                request.getPrice(),
                request.getCapacity(),
                request.getDescription()
        );

        Room savedRoom = roomService.createRoom(room);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RoomResponse.of(savedRoom));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {

        List<RoomResponse> rooms = roomService
                .getAllRooms()
                .stream()
                .map(RoomResponse::of)
                .toList();

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);

        return ResponseEntity.ok(RoomResponse.of(room));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {

        Room updatedData = new Room(
                request.getNumber(),
                request.getType(),
                request.getPrice(),
                request.getCapacity(),
                request.getDescription()
        );

        Room updatedRoom = roomService.updateRoom(id, updatedData);

        return ResponseEntity.ok(RoomResponse.of(updatedRoom));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {

        roomService.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }
}
