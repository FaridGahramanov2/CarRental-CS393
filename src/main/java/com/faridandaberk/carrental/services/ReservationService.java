package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.model.*;
import com.faridandaberk.carrental.repository.CarRepository;
import com.faridandaberk.carrental.repository.EquipmentRepository;
import com.faridandaberk.carrental.repository.LocationRepository;
import com.faridandaberk.carrental.repository.MemberRepository;
import com.faridandaberk.carrental.repository.ReservationRepository;
import com.faridandaberk.carrental.repository.ServiceRepository;
import com.faridandaberk.carrental.struct.ReservationResponseStruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {
    private final CarRepository carRepository;
    private final LocationRepository locationRepository;
    private final MemberRepository memberRepository;
    private final EquipmentRepository equipmentRepository;
    private final ServiceRepository serviceRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(CarRepository carRepository, LocationRepository locationRepository,
                              MemberRepository memberRepository, EquipmentRepository equipmentRepository,
                              ServiceRepository serviceRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.locationRepository = locationRepository;
        this.memberRepository = memberRepository;
        this.equipmentRepository = equipmentRepository;
        this.serviceRepository = serviceRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponseStruct makeReservation(String carBarcode, int dayCount, Long memberId,
                                                     String pickupLocationCode, String dropoffLocationCode,
                                                     List<String> equipmentCodes, List<String> serviceCodes) {
        // Find the car
        Optional<Car> carOptional = carRepository.findByBarcode(carBarcode);
        if (carOptional.isEmpty() || carOptional.get().getStatus() != CarStatus.AVAILABLE) {
            throw new IllegalArgumentException("Car is not available for reservation.");
        }
        Car car = carOptional.get();

        // Find the member
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("Member not found.");
        }
        Member member = memberOptional.get();

        // Find the pickup and dropoff locations
        Optional<Location> pickupLocationOptional = locationRepository.findByCode(pickupLocationCode);
        Optional<Location> dropoffLocationOptional = locationRepository.findByCode(dropoffLocationCode);
        if (pickupLocationOptional.isEmpty() || dropoffLocationOptional.isEmpty()) {
            throw new IllegalArgumentException("Location not found.");
        }
        Location pickupLocation = pickupLocationOptional.get();
        Location dropoffLocation = dropoffLocationOptional.get();

        // Find the requested equipment and services
        List<Equipment> equipment = equipmentRepository.findByNameIn(equipmentCodes);
        List<com.faridandaberk.carrental.model.Service> services = serviceRepository.findByNameIn(serviceCodes);

        // Calculate the total cost
        double totalCost = car.getDailyPrice() * dayCount;
        totalCost += equipment.stream().mapToDouble(Equipment::getPrice).sum();
        totalCost += services.stream().mapToDouble(com.faridandaberk.carrental.model.Service::getPrice).sum();

        // Create the reservation
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setMember(member);
        reservation.setPickUpLocation(pickupLocation);
        reservation.setDropOffLocation(dropoffLocation);
        reservation.setPickUpDate(LocalDateTime.now());
        reservation.setDropOffDate(LocalDateTime.now().plusDays(dayCount));
        reservation.setReturnDate(LocalDateTime.now().plusDays(dayCount));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setEquipment(equipment);
        reservation.setServices(services);
        reservation.setReservationNumber(generateReservationNumber());

        reservationRepository.save(reservation);

        // Create the response struct
        return new ReservationResponseStruct(
                reservation.getReservationNumber(),
                pickupLocation.getName(),
                dropoffLocation.getName(),
                totalCost
        );
    }

    public boolean addServiceToReservation(String reservationNumber, String serviceCode) {
        Optional<Reservation> reservationOptional = reservationRepository.findByReservationNumber(reservationNumber);
        if (reservationOptional.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOptional.get();
        Optional<com.faridandaberk.carrental.model.Service> serviceOptional = serviceRepository.findByCode(serviceCode);
        if (serviceOptional.isEmpty()) {
            return false;
        }

        reservation.getServices().add(serviceOptional.get());
        reservationRepository.save(reservation);
        return true;
    }

    public boolean addEquipmentToReservation(String reservationNumber, String equipmentCode) {
        Optional<Reservation> reservationOptional = reservationRepository.findByReservationNumber(reservationNumber);
        if (reservationOptional.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOptional.get();
        Optional<Equipment> equipmentOptional = equipmentRepository.findByCode(equipmentCode);
        if (equipmentOptional.isEmpty()) {
            return false;
        }

        reservation.getEquipment().add(equipmentOptional.get());
        reservationRepository.save(reservation);
        return true;
    }

    public boolean returnCar(String reservationNumber) {
        Optional<Reservation> reservationOptional = reservationRepository.findByReservationNumber(reservationNumber);
        if (reservationOptional.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOptional.get();
        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.getCar().setStatus(CarStatus.AVAILABLE);
        reservationRepository.save(reservation);
        return true;
    }

    public boolean cancelReservation(String reservationNumber) {
        Optional<Reservation> reservationOptional = reservationRepository.findByReservationNumber(reservationNumber);
        if (reservationOptional.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOptional.get();
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.getCar().setStatus(CarStatus.AVAILABLE);
        reservationRepository.save(reservation);
        return true;
    }

    public boolean deleteReservation(String reservationNumber) {
        Optional<Reservation> reservationOptional = reservationRepository.findByReservationNumber(reservationNumber);
        if (reservationOptional.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOptional.get();
        reservationRepository.delete(reservation);
        return true;
    }

    private String generateReservationNumber() {

        return String.format("%08d", reservationRepository.count() + 1);
    }
}