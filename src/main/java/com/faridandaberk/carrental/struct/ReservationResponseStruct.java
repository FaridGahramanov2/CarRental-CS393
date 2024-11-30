package com.faridandaberk.carrental.struct;

public record ReservationResponseStruct(
        String reservationNumber,
        String pickupLocation,
        String dropoffLocation,
        double totalCost
) {}