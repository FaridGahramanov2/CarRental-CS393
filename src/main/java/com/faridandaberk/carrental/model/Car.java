package com.faridandaberk.carrental.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import java.util.HashSet;
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

    public Car(Long id, String barcode, String licensePlate, Integer passengerCapacity, String brand, String model, Integer mileage, TransmissionType transmissionType, CarType carType, CarStatus status, Double dailyPrice, Set<Reservation> reservations) {
        this.id = id;
        this.barcode = barcode;
        this.licensePlate = licensePlate;
        this.passengerCapacity = passengerCapacity;
        this.brand = brand;
        this.model = model;
        this.mileage = mileage;
        this.transmissionType = transmissionType;
        this.carType = carType;
        this.status = status;
        this.dailyPrice = dailyPrice;
        this.reservations = reservations;
    }





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(Integer passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(TransmissionType transmissionType) {
        this.transmissionType = transmissionType;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public Double getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(Double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private Set<Reservation> reservations = new HashSet<>();

    public Car() {
        this.reservations = new HashSet<>();  // Also initialize in constructor
    }
}



