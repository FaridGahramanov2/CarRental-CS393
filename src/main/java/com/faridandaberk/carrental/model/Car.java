package com.faridandaberk.carrental.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import java.util.Set;



@Entity
public class Car {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String barcode;
    @Column(unique = true, nullable = false)
    private String licensePlate;
    private Integer passengerCapacity;
    private String brand;
    private String model;
    private Integer mileage;
    @Enumerated(EnumType.STRING)
    private TransmissionType transmissionType;
    @Enumerated(EnumType.STRING)
    private CarType carType;
    @Enumerated(EnumType.STRING)
    private CarStatus status;
    private Double dailyPrice;
    @OneToMany(mappedBy = "car")
    private Set<Reservation> reservations;
}

