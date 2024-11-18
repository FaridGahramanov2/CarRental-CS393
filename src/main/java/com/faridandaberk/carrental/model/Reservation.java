package com.faridandaberk.carrental.model;
import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 8)
    private String reservationNumber;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date pickUpDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dropOffDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne @JoinColumn(name = "car_id")
    private Car car;
    @ManyToOne @JoinColumn(name = "pickup_location_id")
    private Location pickUpLocation;
    @ManyToOne @JoinColumn(name = "dropoff_location_id")
    private Location dropOffLocation;

    @ManyToMany
    @JoinTable(name = "reservation_equipment")
    private Set<Equipment> equipment;
    @ManyToMany
    @JoinTable(name = "reservation_service")
    private Set<Service> services;
}
