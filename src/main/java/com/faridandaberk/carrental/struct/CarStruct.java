package com.faridandaberk.carrental.struct;

import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;

public record CarStruct(
        String barcode,
        String brand,
        String model,
        CarType carType,
        TransmissionType transmissionType,
        Integer mileage,
        Double dailyPrice
) {}
