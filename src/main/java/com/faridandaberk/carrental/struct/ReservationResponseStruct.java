package com.faridandaberk.carrental.struct;

import java.util.Date;

public record ReservationResponseStruct(
        String reservationNumber,
        Date pickupDateTime,
        Date dropoffDateTime,
        String pickupLocationCode,
        String pickupLocationName,
        String dropoffLocationCode,
        String dropoffLocationName,
        Double totalAmount
) {}