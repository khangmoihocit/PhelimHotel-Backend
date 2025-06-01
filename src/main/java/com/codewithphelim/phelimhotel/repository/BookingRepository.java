package com.codewithphelim.phelimhotel.repository;

import com.codewithphelim.phelimhotel.model.BookedRoom;
import com.codewithphelim.phelimhotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    Optional<BookedRoom>  findByBookingConfirmationCode(String bookingConfirmationCode);

    List<BookedRoom> room(Room room);

    List<BookedRoom> findByRoomId(Long roomId);
}
