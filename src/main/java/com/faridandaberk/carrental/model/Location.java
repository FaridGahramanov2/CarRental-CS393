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
    @Column(nullable = false)
    private String name;
    private String address;
    @OneToMany(mappedBy = "pickUpLocation")
    private Set<Reservation> pickUpReservations;
    @OneToMany(mappedBy = "dropOffLocation")
    private Set<Reservation> dropOffReservations;
}
