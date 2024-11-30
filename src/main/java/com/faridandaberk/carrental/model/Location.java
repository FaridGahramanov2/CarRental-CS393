package com.faridandaberk.carrental.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;

    public Location(Long id, String code, String name, String address, Set<Reservation> pickUpReservations, Set<Reservation> dropOffReservations) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
        this.pickUpReservations = pickUpReservations;
        this.dropOffReservations = dropOffReservations;
    }
    public Location(){}

    @Column(nullable = false)
    private String name;
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Reservation> getPickUpReservations() {
        return pickUpReservations;
    }

    public void setPickUpReservations(Set<Reservation> pickUpReservations) {
        this.pickUpReservations = pickUpReservations;
    }

    public Set<Reservation> getDropOffReservations() {
        return dropOffReservations;
    }

    public void setDropOffReservations(Set<Reservation> dropOffReservations) {
        this.dropOffReservations = dropOffReservations;
    }

    @OneToMany(mappedBy = "pickUpLocation")
    private Set<Reservation> pickUpReservations;
    @OneToMany(mappedBy = "dropOffLocation")
    private Set<Reservation> dropOffReservations;
}
