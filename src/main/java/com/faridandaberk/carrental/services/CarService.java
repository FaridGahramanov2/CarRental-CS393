package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.model.Car;
import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;
import com.faridandaberk.carrental.struct.CarStruct;
import com.faridandaberk.carrental.struct.RentedCarStruct;
import org.springframework.stereotype.Service;
import jakarta.persistence.*;

import java.util.List;


@Service
public class CarService {
    //private final CarRepository carRepository;   ----ToDO waiting repo

    public List<CarStruct> searchAvailableCars(CarType carType, TransmissionType transmissionType) {
        return null;
    }

    public List<RentedCarStruct> getAllRentedCars() {
        return null;
    }

    public boolean deleteCar(String barcode) {
        return false;
    }

    public Car addCar(CarStruct carDTO) {
        return null;
    }
}
