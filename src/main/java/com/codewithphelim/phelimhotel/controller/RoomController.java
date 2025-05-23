package com.codewithphelim.phelimhotel.controller;

import com.codewithphelim.phelimhotel.exception.OurException;
import com.codewithphelim.phelimhotel.model.BookedRoom;
import com.codewithphelim.phelimhotel.model.Room;
import com.codewithphelim.phelimhotel.response.BookingResponse;
import com.codewithphelim.phelimhotel.response.RoomResponse;
import com.codewithphelim.phelimhotel.service.BookingService;
import com.codewithphelim.phelimhotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private BookingService bookingService;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile photo,
                                                   @RequestParam("roomType") String roomType,
                                                   @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room-types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/get-all-room")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    //trả về phòng, phòng này chứa các phòng được đặt
    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = bookingService.getAllBookingByRoomId(room.getId()); //danh sách phòng đang được đặt trong database
        List<BookingResponse> bookingResponses = bookings //danh sách phòng được đặt cho client
                .stream()
                .map(booking -> new BookingResponse(
                        booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getBookingConfirmationCode()))
                .toList();
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if(photoBlob != null){
            try{
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            }
            catch (SQLException e){
                throw new OurException(e.getMessage());
            }
        }
        return new RoomResponse(bookingResponses, room.isBooked(), room.getRoomType(), room.getRoomPrice(), photoBytes, room.getId());
    }
}
