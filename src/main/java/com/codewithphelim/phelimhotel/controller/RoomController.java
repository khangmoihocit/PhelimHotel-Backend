package com.codewithphelim.phelimhotel.controller;

import com.codewithphelim.phelimhotel.exception.OurException;
import com.codewithphelim.phelimhotel.model.BookedRoom;
import com.codewithphelim.phelimhotel.model.Room;
import com.codewithphelim.phelimhotel.response.BookingResponse;
import com.codewithphelim.phelimhotel.response.RoomResponse;
import com.codewithphelim.phelimhotel.service.BookingService;
import com.codewithphelim.phelimhotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoomById(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get-all-room")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(value = "photo", required = false) MultipartFile photo,
                                                   @RequestParam(value = "roomType", required = false) String roomType,
                                                   @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice) throws IOException, SQLException {
        //ảnh được truyền vào để cập nhật khác null thì lấy ảnh đó
        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);

        //blob để lưu vào database
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        theRoom.setPhoto(photoBlob);
        RoomResponse roomResponse = getRoomResponse(theRoom);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId){
        Optional<Room> theRoom = roomService.getRoomById(roomId);

        return theRoom.map(room -> {
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(roomResponse);
        }).orElseThrow(()->new OurException("Không tìm thấy phòng"));
    }

    //map room entity sang room response
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
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new OurException(e.getMessage());
            }
        }
        return new RoomResponse(bookingResponses, room.isBooked(), room.getRoomType(), room.getRoomPrice(), photoBytes, room.getId());
    }


}
