package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.model.*;
import com.faridandaberk.carrental.repository.CarRepository;
import com.faridandaberk.carrental.struct.CarStruct;
import com.faridandaberk.carrental.struct.RentedCarStruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.Duration;

@Service
@Transactional
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarStruct> searchAvailableCars(CarType carType, TransmissionType transmissionType) {
        return carRepository.findByCarTypeAndTransmissionTypeAndStatus(carType, transmissionType, CarStatus.AVAILABLE)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RentedCarStruct> getAllRentedCars() {
        List<Car> rentedCars = carRepository.findByStatusIn(Arrays.asList(CarStatus.LOANED, CarStatus.RESERVED));
        return rentedCars.stream()
                .map(car -> {
                    Reservation reservation = car.getReservations().stream()
                            .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                            .findFirst()
                            .orElse(null);
                    if (reservation == null) return null;

                    LocalDateTime pickUpDate = reservation.getPickUpDate();
                    LocalDateTime dropOffDate = reservation.getDropOffDate();
                    long daysDiff = Duration.between(pickUpDate, dropOffDate).toDays();

                    return new RentedCarStruct(
                            car.getBrand(),
                            car.getModel(),
                            car.getCarType(),
                            car.getTransmissionType(),
                            car.getBarcode(),
                            reservation.getReservationNumber(),
                            reservation.getMember().getName(),
                            reservation.getDropOffDate(),
                            reservation.getDropOffLocation().getName(),
                            daysDiff
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean deleteCar(String barcode) {
        Optional<Car> car = carRepository.findByBarcode(barcode);
        if (car.isPresent() && car.get().getStatus() == CarStatus.AVAILABLE) {
            carRepository.delete(car.get());
            return true;
        }
        return false;
    }

    public CarStruct addCar(CarStruct carDTO) {
        Car car = new Car();
        car.setBarcode(carDTO.barcode());
        car.setBrand(carDTO.brand());
        car.setModel(carDTO.model());
        car.setCarType(carDTO.carType());
        car.setTransmissionType(carDTO.transmissionType());
        car.setPassengerCapacity(carDTO.passengerCapacity());
        car.setDailyPrice(carDTO.dailyPrice());
        car.setMileage(carDTO.mileage());
        car.setLicensePlate(carDTO.licensePlate());
        car.setStatus(CarStatus.AVAILABLE);

        Car savedCar = carRepository.save(car);
        return mapToDTO(savedCar);
    }

    private CarStruct mapToDTO(Car car) {
        return new CarStruct(
                car.getBrand(),
                car.getModel(),
                car.getBarcode(),
                car.getCarType(),
                car.getTransmissionType(),
                car.getPassengerCapacity(),
                car.getDailyPrice(),
                car.getMileage(),
                car.getLicensePlate()
        );
    }
}