package com.codewithphelim.phelimhotel.service;

import com.codewithphelim.phelimhotel.model.BookedRoom;

import java.util.List;

public interface BookingService {
    List<BookedRoom> getAllBookingByRoomId(Long roomId);
}
