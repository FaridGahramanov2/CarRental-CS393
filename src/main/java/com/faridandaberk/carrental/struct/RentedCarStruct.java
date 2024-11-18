package com.faridandaberk.carrental.struct;

import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;

import java.util.Date;

public record RentedCarStruct(
        String brand,
        String model,
        CarType carType,
        TransmissionType transmissionType,
        String barcode,
        String reservationNumber,
        String memberName,
        Date dropOffDateTime,
        String dropOffLocation,
        Integer dayCount
) {}