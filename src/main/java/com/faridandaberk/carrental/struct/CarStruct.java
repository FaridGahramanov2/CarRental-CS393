package com.faridandaberk.carrental.struct;

import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;

public record CarStruct(
        String brand,
        String model,
        String barcode,

        CarType carType,
        TransmissionType transmissionType,
        int passengerCapacity,
        double dailyPrice,
        int mileage,
        String licensePlate
) {}