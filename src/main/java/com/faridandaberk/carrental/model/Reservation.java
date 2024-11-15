package com.faridandaberk.carrental.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reservationNumber;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickUpDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dropOffDate;
    private String pickUpLocation;
    private String dropOffLocation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;
    private String status;

    @ManyToOne
    private Member member;
    @ManyToOne
    private Car car;

}
