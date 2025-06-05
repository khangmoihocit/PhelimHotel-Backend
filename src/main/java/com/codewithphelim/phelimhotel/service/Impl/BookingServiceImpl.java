package com.codewithphelim.phelimhotel.service.Impl;

import com.codewithphelim.phelimhotel.exception.InvalidBookingRequestException;
import com.codewithphelim.phelimhotel.exception.OurException;
import com.codewithphelim.phelimhotel.model.BookedRoom;
import com.codewithphelim.phelimhotel.model.Room;
import com.codewithphelim.phelimhotel.repository.BookingRepository;
import com.codewithphelim.phelimhotel.response.BookingResponse;
import com.codewithphelim.phelimhotel.service.BookingService;
import com.codewithphelim.phelimhotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomService roomService;

    @Override
    public List<BookedRoom> getAllBookingByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (!bookingRequest.getCheckInDate().isBefore(bookingRequest.getCheckOutDate())) {
            throw new InvalidBookingRequestException("Ngày nhận phòng phải trước ngày trả phòng!");
        }

        //đặt phòng phải xem phòng được đặt vào ngày đấy có trống không, 1 phòng được đặt nhiều lần
        Room room = roomService.getRoomById(roomId).get();
        boolean roomIsAvailabel = roomIsAvailable(bookingRequest, room.getBookings());

        if(roomIsAvailabel){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw new InvalidBookingRequestException("Xin lỗi, phòng này hiện không còn chỗ trống, bạn hãy chọn ngày khác");
        }

        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedRoom findByConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new OurException("Lỗi, không tìm thấy đặt phòng có code là: " + confirmationCode));
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {

        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
