package com.faridandaberk.carrental.config;

import com.faridandaberk.carrental.model.*;
import com.faridandaberk.carrental.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {


    private final  ReservationRepository reservationRepository;
    private final LocationRepository locationRepository;
    private final CarRepository carRepository;
    private final EquipmentRepository equipmentRepository;
    private final ServiceRepository serviceRepository;
    private final MemberRepository memberRepository;

    public DataInitializer(ReservationRepository reservationRepository, LocationRepository locationRepository,
                           CarRepository carRepository,
                           EquipmentRepository equipmentRepository,
                           ServiceRepository serviceRepository,
                           MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.locationRepository = locationRepository;
        this.carRepository = carRepository;
        this.equipmentRepository = equipmentRepository;
        this.serviceRepository = serviceRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initializeLocations();
        initializeCars();
        initializeEquipment();
        initializeServices();
        initializeMembers();
        initializeReservations();
    }

    private void initializeReservations() {

        Car car = carRepository.findByBarcode("CAR001").orElseThrow();
        Member member = memberRepository.findByEmail("john@example.com").orElseThrow();
        Location pickupLoc = locationRepository.findByCode("1").orElseThrow();
        Location dropoffLoc = locationRepository.findByCode("2").orElseThrow();

        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setMember(member);
        reservation.setPickUpLocation(pickupLoc);
        reservation.setDropOffLocation(dropoffLoc);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setPickUpDate(LocalDateTime.now());
        reservation.setDropOffDate(LocalDateTime.now().plusDays(3));
        reservation.setReservationNumber("00000001");


        List<Equipment> equipment = equipmentRepository.findAll();
        List<Service> services = serviceRepository.findAll();
        reservation.setEquipment(equipment);
        reservation.setServices(services);

        reservationRepository.save(reservation);
    }

    private void initializeLocations() {
        Location location1 = new Location();
        location1.setCode("1");
        location1.setName("İstanbul Airport");

        Location location2 = new Location();
        location2.setCode("2");
        location2.setName("İstanbul Sabiha Gökçen Airport");

        Location location3 = new Location();
        location3.setCode("3");
        location3.setName("İstanbul Kadıköy");

        locationRepository.saveAll(Arrays.asList(location1, location2, location3));
    }

    private void initializeCars() {
        Car car1 = new Car();
        car1.setBarcode("CAR001");
        car1.setBrand("Toyota");
        car1.setModel("Corolla");
        car1.setCarType(CarType.STANDARD);
        car1.setTransmissionType(TransmissionType.AUTOMATIC);
        car1.setDailyPrice(100.0);
        car1.setLicensePlate("34ABC123");
        car1.setPassengerCapacity(5);
        car1.setMileage(50000);
        car1.setStatus(CarStatus.AVAILABLE);

        Car car2 = new Car();
        car2.setBarcode("CAR002");
        car2.setBrand("BMW");
        car2.setModel("X5");
        car2.setCarType(CarType.SUV);
        car2.setTransmissionType(TransmissionType.AUTOMATIC);
        car2.setDailyPrice(200.0);
        car2.setLicensePlate("34XYZ789");
        car2.setPassengerCapacity(7);
        car2.setMileage(30000);
        car2.setStatus(CarStatus.AVAILABLE);

        carRepository.saveAll(Arrays.asList(car1, car2));
    }

    private void initializeEquipment() {
        Equipment equipment1 = new Equipment(null, "EQ001", "GPS", 10.0, new HashSet<>());
        equipment1.setCode("EQ001"); // Add this
        equipment1.setName("GPS");
        equipment1.setPrice(10.0);

        Equipment equipment2 = new Equipment(null, "EQ002", "Child Seat", 15.0, new HashSet<>());
        equipment2.setCode("EQ002"); // Add this
        equipment2.setName("Child Seat");
        equipment2.setPrice(15.0);

        equipmentRepository.saveAll(Arrays.asList(equipment1, equipment2));
    }

    private void initializeServices() {
        Service service1 = new Service();
        service1.setCode("SRV001");  // Add this
        service1.setName("Additional Driver");
        service1.setPrice(30.0);

        Service service2 = new Service();
        service2.setCode("SRV002");  // Add this
        service2.setName("Roadside Assistance");
        service2.setPrice(25.0);

        serviceRepository.saveAll(Arrays.asList(service1, service2));
    }

    private void initializeMembers() {
        Member member1 = new Member();
        member1.setName("John Doe");
        member1.setEmail("john@example.com");
        member1.setPhone("5551234567");
        member1.setAddress("123 Main St");
        member1.setDrivingLicenseNumber("DL123456");

        memberRepository.save(member1);
    }
}
