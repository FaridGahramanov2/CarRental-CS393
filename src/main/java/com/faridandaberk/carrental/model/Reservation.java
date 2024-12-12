package com.faridandaberk.carrental.model;
import ch.qos.logback.classic.layout.TTLLLayout;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
public class Reservation {
    public Reservation(Long id, String reservationNumber, LocalDateTime creationDate, LocalDateTime pickUpDate, LocalDateTime dropOffDate, LocalDateTime returnDate, ReservationStatus status, Member member, Car car, Location pickUpLocation, Location dropOffLocation, List<Equipment> equipment, List<Service> services) {
        this.id = id;
        this.reservationNumber = reservationNumber;
        this.creationDate = creationDate;
        this.pickUpDate = pickUpDate;
        this.dropOffDate = dropOffDate;
        this.returnDate = returnDate;
        this.status = status;
        this.member = member;
        this.car = car;
        this.pickUpLocation = pickUpLocation;
        this.dropOffLocation = dropOffLocation;
        this.equipment = equipment;
        this.services = services;
    }
    public Reservation(){}

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 8)
    private String reservationNumber;

    private LocalDateTime creationDate;

    private LocalDateTime pickUpDate;

    private LocalDateTime dropOffDate;

    private LocalDateTime returnDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(LocalDateTime pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public LocalDateTime getDropOffDate() {
        return dropOffDate;
    }

    public void setDropOffDate(LocalDateTime dropOffDate) {
        this.dropOffDate = dropOffDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Location getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(Location pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Location getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(Location dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }




    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }


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
    private List<Equipment> equipment;
    @ManyToMany
    @JoinTable(name = "reservation_service")
    private List<Service> services;
}
