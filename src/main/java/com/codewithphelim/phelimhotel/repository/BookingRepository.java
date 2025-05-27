package com.codewithphelim.phelimhotel.repository;

import com.codewithphelim.phelimhotel.model.BookedRoom;
import com.codewithphelim.phelimhotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    BookedRoom findByBookingConfirmationCode(String bookingConfirmationCode);

    List<BookedRoom> room(Room room);

    List<BookedRoom> findByRoomId(Long roomId);
}
