package com.codewithphelim.phelimhotel.response;

import com.codewithphelim.phelimhotel.model.BookedRoom;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private String photo;
    private List<BookingResponse> bookings;

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

