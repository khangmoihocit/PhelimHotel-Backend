package com.codewithphelim.phelimhotel.service;

import com.codewithphelim.phelimhotel.model.BookedRoom;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    List<BookedRoom> getAllBookingByRoomId(Long roomId);
    void cancelBooking(Long bookingId);
    String saveBooking(Long roomId, BookedRoom bookingRequest);
    BookedRoom findByConfirmationCode(String confirmationCode);
    List<BookedRoom> getAllBookings();
}
