package com.kalamburya.booking_system.service;

import com.kalamburya.booking_system.entity.Room;
import com.kalamburya.booking_system.exception.RoomNotFoundException;
import com.kalamburya.booking_system.repository.RoomRepository;

import java.util.List;

public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository roomRepository){
        repository = roomRepository;
    }

    public Room createRoom(Room room){
        if (repository.existsByNumber(room.getNumber())) {
            throw new IllegalArgumentException("Room with number " + room.getNumber() + "already exists");
        } else {
            return repository.save(room);
        }
    }

    public Room getRoomById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    public List<Room> getAllRooms() {
        return repository.findAll();
    }

    public Room updateRoom(Long id, Room roomToUpdate){

        Room oldRoom = getRoomById(id);

        oldRoom.setNumber(roomToUpdate.getNumber());
        oldRoom.setType(roomToUpdate.getType());
        oldRoom.setPrice(roomToUpdate.getPrice());
        oldRoom.setCapacity(roomToUpdate.getCapacity());
        oldRoom.setDescription(roomToUpdate.getDescription());

        return repository.save(oldRoom);
    }

    public void deleteRoom(Long id){

        Room roomToDelete = getRoomById(id);

        repository.delete(roomToDelete);
    }

}
