package com.faridandaberk.carrental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    public Member(Long id, String name, String address, String email, String phone, String drivingLicenseNumber, Set<Reservation> reservations) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.reservations = reservations;
    }
    public Member(String name, String email, String phone, String address, String drivingLicenseNumber, Set<Reservation> reservations) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.reservations = reservations;
    }
    public Member() {

    }

    private String address;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(unique = true, nullable = false)
    private String drivingLicenseNumber;
    @OneToMany(mappedBy = "member")
    private Set<Reservation> reservations;
}