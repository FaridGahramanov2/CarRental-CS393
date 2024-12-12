package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.exception.ResourceNotFoundException;
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
    private final ReservationRepository reservationRepository;

    public CarService(CarRepository carRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<CarStruct> searchAvailableCars(CarType carType, TransmissionType transmissionType) {
        List<Car> availableCars = carRepository.findByCarTypeAndTransmissionTypeAndStatus(
                carType,
                transmissionType,
                CarStatus.AVAILABLE
        );

        if (availableCars.isEmpty()) {
            throw new ResourceNotFoundException("No available cars found with the specified criteria");
        }

        return availableCars.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<RentedCarStruct> getAllRentedCars() {
        List<Car> rentedCars = carRepository.findByStatusIn(Arrays.asList(CarStatus.LOANED, CarStatus.RESERVED));

        if (rentedCars.isEmpty()) {
            throw new ResourceNotFoundException("No rented cars found");
        }

        List<Reservation> activeReservations = reservationRepository.findByStatus(ReservationStatus.ACTIVE);
        List<RentedCarStruct> result = new ArrayList<>();

        for (Car car : rentedCars) {
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
        Car car = carRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with barcode: " + barcode));

        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new IllegalStateException("Car cannot be deleted as it is not available");
        }

        // Check if car was ever used in a reservation
        List<Reservation> carReservations = reservationRepository.findByCar(car);
        if (!carReservations.isEmpty()) {
            throw new IllegalStateException("Car cannot be deleted as it has reservation history");
        }

        carRepository.delete(car);
        return true;
    }

    public CarStruct addCar(CarStruct carDTO) {
        // Validate input
        if (carDTO == null) {
            throw new IllegalArgumentException("Car data cannot be null");
        }

        // Check if car with same barcode already exists
        if (carRepository.findByBarcode(carDTO.barcode()).isPresent()) {
            throw new IllegalStateException("Car with this barcode already exists");
        }

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
        car.setReservations(new HashSet<>());

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