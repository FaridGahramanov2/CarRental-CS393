package com.faridandaberk.carrental.controller;

import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;
import com.faridandaberk.carrental.services.CarService;
import com.faridandaberk.carrental.struct.CarStruct;
import com.faridandaberk.carrental.struct.RentedCarStruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<CarStruct>> searchAvailableCars(
            @RequestParam CarType carType,
            @RequestParam TransmissionType transmissionType) {
        List<CarStruct> cars = carService.searchAvailableCars(carType, transmissionType);
        return cars.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cars);
    }

    @GetMapping("/rented")
    public ResponseEntity<List<RentedCarStruct>> getRentedCars() {
        List<RentedCarStruct> cars = carService.getAllRentedCars();
        return cars.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cars);
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<Boolean> deleteCar(@PathVariable String barcode) {
        return carService.deleteCar(barcode) ?
                ResponseEntity.ok(true) :
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(false);
    }

    @PostMapping
    public ResponseEntity<CarStruct> addCar(@RequestBody CarStruct carDTO) {
        return ResponseEntity.ok(carService.addCar(carDTO));
    }
}