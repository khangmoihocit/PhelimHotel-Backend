package com.codewithphelim.phelimhotel.service;

import com.codewithphelim.phelimhotel.model.Room;
import com.codewithphelim.phelimhotel.response.RoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SQLException, IOException;
    List<String> getAllRoomTypes();
    List<RoomResponse> getAllRooms() throws SQLException;
    void deleteRoomById(Long id);
    byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException;
    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);
    Optional<Room> getRoomById(Long roomId);
}
