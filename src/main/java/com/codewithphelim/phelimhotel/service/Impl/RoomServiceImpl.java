package com.codewithphelim.phelimhotel.service.Impl;

import com.codewithphelim.phelimhotel.exception.OurException;
import com.codewithphelim.phelimhotel.model.Room;
import com.codewithphelim.phelimhotel.repository.RoomRepository;
import com.codewithphelim.phelimhotel.response.RoomResponse;
import com.codewithphelim.phelimhotel.service.RoomService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        //chuyen file sang blob
        if(!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }

        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    private byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isEmpty()){
            throw new OurException("không tìm thấy phòng");
        }
        Blob photoBlob = room.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public List<RoomResponse> getAllRooms() throws SQLException {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room : rooms){
            byte[] photoBytes = getRoomPhotoByRoomId(room.getId());
            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setId(room.getId());
            roomResponse.setRoomType(room.getRoomType());
            roomResponse.setRoomPrice(room.getRoomPrice());
            if(photoBytes != null && photoBytes.length > 0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                roomResponse.setPhoto(base64Photo);
            }
            roomResponses.add(roomResponse);
        }
        return roomResponses;
    }


}
