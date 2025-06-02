package com.codewithphelim.phelimhotel.repository;

import com.codewithphelim.phelimhotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select distinct r.roomType from Room r")
    List<String> findDistinctRoomTypes();

    @Query("select Room from Room where Room.roomType like %:roomType%" +
            "and Room.id not in (" +
                "select BookedRoom.room.id " +
                "from BookedRoom " +
                "where BookedRoom.checkInDate <= :checkOutDate) and BookedRoom.checkOutDate >= :checkInDate")
    List<Room> findAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);


}
