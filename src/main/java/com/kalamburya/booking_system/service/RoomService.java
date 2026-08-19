package com.kalamburya.booking_system.service;

import com.kalamburya.booking_system.entity.Room;
import com.kalamburya.booking_system.exception.RoomNotFoundException;
import com.kalamburya.booking_system.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public Room updateRoom(Long id, Room newRoom){

        Room roomToUpdate = getRoomById(id);

        roomToUpdate.setNumber(newRoom.getNumber());
        roomToUpdate.setType(newRoom.getType());
        roomToUpdate.setPrice(newRoom.getPrice());
        roomToUpdate.setCapacity(newRoom.getCapacity());
        roomToUpdate.setDescription(newRoom.getDescription());

        return repository.save(roomToUpdate);
    }

    public void deleteRoom(Long id){

        Room roomToDelete = getRoomById(id);

        repository.delete(roomToDelete);
    }

}
