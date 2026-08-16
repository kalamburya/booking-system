package com.kalamburya.booking_system.repository;

import com.kalamburya.booking_system.entity.Room;
import com.kalamburya.booking_system.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByNumber(String number);

    List<Room> findByType(RoomType type);

    boolean existsByNumber(String number);

}
