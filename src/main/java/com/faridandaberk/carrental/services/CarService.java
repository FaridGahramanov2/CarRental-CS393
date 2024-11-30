package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.model.*;
import com.faridandaberk.carrental.repository.CarRepository;
import com.faridandaberk.carrental.repository.ReservationRepository;
import com.faridandaberk.carrental.struct.CarStruct;
import com.faridandaberk.carrental.struct.RentedCarStruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.Duration;

@Service
@Transactional
public class CarService {
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository; // Add this

    public CarService(CarRepository carRepository, ReservationRepository reservationRepository) { // Update constructor
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<CarStruct> searchAvailableCars(CarType carType, TransmissionType transmissionType) {
        return carRepository.findByCarTypeAndTransmissionTypeAndStatus(carType, transmissionType, CarStatus.AVAILABLE)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RentedCarStruct> getAllRentedCars() {
        List<Car> rentedCars = carRepository.findByStatusIn(Arrays.asList(CarStatus.LOANED, CarStatus.RESERVED));
        List<Reservation> activeReservations = reservationRepository.findByStatus(ReservationStatus.ACTIVE);
        List<RentedCarStruct> result = new ArrayList<>();

        for (Car car : rentedCars) {
            // Find active reservation for this car from our list
            Reservation activeReservation = activeReservations.stream()
                    .filter(res -> res.getCar().getId().equals(car.getId()))
                    .findFirst()
                    .orElse(null);

            if (activeReservation != null) {
                long daysDiff = Duration.between(
                        activeReservation.getPickUpDate(),
                        activeReservation.getDropOffDate()
                ).toDays();

                RentedCarStruct rentedCar = new RentedCarStruct(
                        car.getBrand(),
                        car.getModel(),
                        car.getCarType(),
                        car.getTransmissionType(),
                        car.getBarcode(),
                        activeReservation.getReservationNumber(),
                        activeReservation.getMember().getName(),
                        activeReservation.getDropOffDate(),
                        activeReservation.getDropOffLocation().getName(),
                        daysDiff
                );
                result.add(rentedCar);
            }
        }

        return result;
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
        car.setReservations(new HashSet<>()); // Initialize reservations set

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