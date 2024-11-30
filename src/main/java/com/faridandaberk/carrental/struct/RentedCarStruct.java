package com.faridandaberk.carrental.struct;

import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;
import java.time.LocalDateTime;



import java.time.LocalDate;
import java.util.Date;

public record RentedCarStruct(
        String brand,
        String model,
        CarType carType,
        TransmissionType transmissionType,
        String barcode,

        String reservationNumber,
        String memberName,
        LocalDateTime dropOffDateTime,
        String dropOffLocation,
        long dayCount


) {}