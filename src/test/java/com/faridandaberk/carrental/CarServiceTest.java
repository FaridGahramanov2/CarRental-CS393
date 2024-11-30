package com.faridandaberk.carrental;

import com.faridandaberk.carrental.model.*;
import com.faridandaberk.carrental.repository.CarRepository;
import com.faridandaberk.carrental.repository.LocationRepository;
import com.faridandaberk.carrental.repository.MemberRepository;
import com.faridandaberk.carrental.repository.ReservationRepository;
import com.faridandaberk.carrental.services.CarService;
import com.faridandaberk.carrental.services.MemberService;
import com.faridandaberk.carrental.services.ReservationService;
import com.faridandaberk.carrental.struct.CarStruct;
import com.faridandaberk.carrental.struct.MemberStruct;
import com.faridandaberk.carrental.struct.RentedCarStruct;
import com.faridandaberk.carrental.struct.ReservationResponseStruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CarServiceTest {

    @Autowired
    private CarService carService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;




    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;



    @Autowired
    private CarRepository carRepository;

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

        Location location1 = new Location();
        location1.setCode("TEST_LOC_1");
        location1.setName("Test Location 1");
        locationRepository.save(location1);

        Location location2 = new Location();
        location2.setCode("TEST_LOC_2");
        location2.setName("Test Location 2");
        locationRepository.save(location2);


        CarStruct carDTO = new CarStruct(
                "Toyota",
                "Corolla",
                "TEST123",
                CarType.STANDARD,
                TransmissionType.MANUAL,
                5,
                100.0,
                15000,
                "34TEST123"
        );
        CarStruct savedCar = carService.addCar(carDTO);


        MemberStruct memberDTO = new MemberStruct(
                "Test Member",
                "123 Test St",
                "test@example.com",
                "5551234567",
                "TEST_DL_123"
        );
        MemberStruct savedMember = memberService.registerMember(memberDTO);


        Member member = memberRepository.findByEmail("test@example.com")
                .orElseThrow(() -> new RuntimeException("Member not found"));


        ReservationResponseStruct reservation = reservationService.makeReservation(
                savedCar.barcode(),
                3,
                member.getId(),
                "TEST_LOC_1",
                "TEST_LOC_2",
                List.of(),
                List.of()
        );

        List<RentedCarStruct> result = carService.getAllRentedCars();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).barcode()).isEqualTo(savedCar.barcode());
    }
    @Test
    void deleteCar_CarUsedInReservation_ReturnsFalse() {

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
        car.setStatus(CarStatus.LOANED);
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