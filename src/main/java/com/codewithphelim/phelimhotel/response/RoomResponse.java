package com.codewithphelim.phelimhotel.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private String photo; //trả về client url ảnh
    private List<BookingResponse> bookings; //1 phòng chứa nhiều đặt phòng | danh sách phòng này từng được đặt(lịch sử)

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(List<BookingResponse> bookings, boolean isBooked, String roomType, BigDecimal roomPrice, byte[] photoBytes, Long id) {
        this.bookings = bookings;
        this.isBooked = isBooked;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
        this.id = id;
    }
}

