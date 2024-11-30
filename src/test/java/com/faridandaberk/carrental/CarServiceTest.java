package com.faridandaberk.carrental;

import com.faridandaberk.carrental.model.*;
import com.faridandaberk.carrental.repository.CarRepository;
import com.faridandaberk.carrental.repository.LocationRepository;
import com.faridandaberk.carrental.repository.MemberRepository;
import com.faridandaberk.carrental.repository.ReservationRepository;
import com.faridandaberk.carrental.services.CarService;
import com.faridandaberk.carrental.struct.CarStruct;
import com.faridandaberk.carrental.struct.RentedCarStruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CarServiceTest {

    @Autowired
    private ReservationRepository reservationRepository;


    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;  // Added for test setup

    @Test
    void searchAvailableCars_FindsNoCars_ReturnsEmptyList() {
        List<CarStruct> result = carService.searchAvailableCars(CarType.STANDARD, TransmissionType.MANUAL);
        assertThat(result).isEmpty();
    }

    @Test
    void searchAvailableCars_FindsCars_ReturnsNonEmptyList() {
        CarStruct carDTO = new CarStruct(
                "Toyota",
                "Corolla",
                "123ABC",
                CarType.STANDARD,
                TransmissionType.MANUAL,
                5,
                100.0,
                15000,
                "34TEST123"
        );
        carService.addCar(carDTO);

        List<CarStruct> result = carService.searchAvailableCars(CarType.STANDARD, TransmissionType.MANUAL);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).barcode()).isEqualTo("123ABC");
    }

    @Test
    void deleteCar_CarAvailable_ReturnsTrue() {
        CarStruct carDTO = new CarStruct(
                "Toyota",
                "Corolla",
                "123ABC",
                CarType.STANDARD,
                TransmissionType.MANUAL,
                5,
                100.0,
                15000,
                "34TEST123"
        );
        carService.addCar(carDTO);

        boolean result = carService.deleteCar("123ABC");
        assertThat(result).isTrue();
    }

    @Test
    void getAllRentedCars_WithRentedCars_ReturnsNonEmptyList() {
        // Create and save a car with LOANED status
        Car car = new Car();
        car.setBarcode("TEST123");
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setCarType(CarType.STANDARD);
        car.setTransmissionType(TransmissionType.MANUAL);
        car.setPassengerCapacity(5);
        car.setDailyPrice(100.0);
        car.setMileage(15000);
        car.setLicensePlate("34TEST123");
        car.setStatus(CarStatus.LOANED);
        car.setReservations(new HashSet<>());
        car = carRepository.save(car); // Save and get the managed entity

        // Create necessary related entities
        Location location = new Location();
        location.setCode("LOC1");
        location.setName("Test Location");
        location = locationRepository.save(location); // Save and get the managed entity

        // Create member with all required fields
        Member member = new Member();
        member.setName("Test Member");
        member.setEmail("test@example.com");
        member.setPhone("5551234567");
        member.setAddress("123 Test St");
        member.setDrivingLicenseNumber("TEST_DL_123");
        member = memberRepository.save(member); // Save and get the managed entity

        // Create and add an active reservation
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setMember(member);
        reservation.setDropOffLocation(location);
        reservation.setPickUpDate(LocalDateTime.now());
        reservation.setDropOffDate(LocalDateTime.now().plusDays(3));
        reservation.setReservationNumber("RES123");

        // Save the reservation
        reservation = reservationRepository.save(reservation);

        // Update car's reservations set
        car.getReservations().add(reservation);
        carRepository.save(car);

        List<RentedCarStruct> result = carService.getAllRentedCars();
        assertThat(result).isNotEmpty();
    }

    @Test
    void deleteCar_CarUsedInReservation_ReturnsFalse() {
        // Create and save a car with LOANED status
        Car car = new Car();
        car.setBarcode("TEST123");
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setCarType(CarType.STANDARD);
        car.setTransmissionType(TransmissionType.MANUAL);
        car.setPassengerCapacity(5);
        car.setDailyPrice(100.0);
        car.setMileage(15000);
        car.setLicensePlate("TEST_DL_123");
        car.setStatus(CarStatus.LOANED);  // Set status to LOANED to simulate car in use
        carRepository.save(car);

        boolean result = carService.deleteCar("TEST123");
        assertThat(result).isFalse();
    }

    @Test
    void addCar_ValidCar_ReturnsCarDTO() {
        CarStruct carDTO = new CarStruct(
                "Toyota",
                "Corolla",
                "123ABC",
                CarType.STANDARD,
                TransmissionType.MANUAL,
                5,
                100.0,
                15000,
                "34TEST123"
        );

        CarStruct result = carService.addCar(carDTO);

        assertThat(result).isNotNull();
        assertThat(result.barcode()).isEqualTo(carDTO.barcode());
        assertThat(result.brand()).isEqualTo(carDTO.brand());
        assertThat(result.model()).isEqualTo(carDTO.model());
    }
}