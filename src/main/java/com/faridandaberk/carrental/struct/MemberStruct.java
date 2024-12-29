package com.faridandaberk.carrental.struct;

public record MemberStruct(
        String name,
        String email,
        String phone,
        String address,
        String drivingLicenseNumber,
        String password    // Added password field
) {
    // Custom constructor for cases where password isn't needed
    public MemberStruct(String name, String email, String phone, String address, String drivingLicenseNumber) {
        this(name, email, phone, address, drivingLicenseNumber, null);
    }
}