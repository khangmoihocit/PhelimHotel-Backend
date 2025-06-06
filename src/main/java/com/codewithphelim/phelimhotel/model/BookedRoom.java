package com.codewithphelim.phelimhotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestFullName;
    private String guestEmail;
    private int numberOfAdults;
    private int numberOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalNumOfGuest(){
        this.totalNumOfGuest = this.numberOfAdults + this.numberOfChildren;
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numberOfChildren = numOfChildren;
        calculateTotalNumOfGuest();
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numberOfAdults = numOfAdults;
        calculateTotalNumOfGuest();
    }


}
