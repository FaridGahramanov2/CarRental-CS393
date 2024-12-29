package com.faridandaberk.carrental.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private String address;

    @Column(unique = true, nullable = false)
    private String drivingLicenseNumber;

    @Column(nullable = true)
    private String password;

    @OneToMany(mappedBy = "member")
    private Set<Reservation> reservations = new HashSet<>();

    // Default constructor
    public Member() {
    }

    // Constructor with id
    public Member(Long id, String name, String address, String email, String phone, String drivingLicenseNumber, Set<Reservation> reservations) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.reservations = reservations != null ? reservations : new HashSet<>();
    }

    // Constructor without id
    public Member(String name, String email, String phone, String address, String drivingLicenseNumber, Set<Reservation> reservations) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.reservations = reservations != null ? reservations : new HashSet<>();
    }

    // Getters and Setters
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations != null ? reservations : new HashSet<>();
    }

    // Helper method to add a reservation
    public void addReservation(Reservation reservation) {
        if (reservations == null) {
            reservations = new HashSet<>();
        }
        reservations.add(reservation);
        reservation.setMember(this);
    }

    // Helper method to remove a reservation
    public void removeReservation(Reservation reservation) {
        if (reservations != null) {
            reservations.remove(reservation);
            reservation.setMember(null);
        }
    }
}