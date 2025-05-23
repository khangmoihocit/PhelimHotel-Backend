package com.codewithphelim.phelimhotel.service.Impl;

import com.codewithphelim.phelimhotel.model.BookedRoom;
import com.codewithphelim.phelimhotel.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Override
    public List<BookedRoom> getAllBookingByRoomId(Long roomId) {

        return List.of();
    }
}
