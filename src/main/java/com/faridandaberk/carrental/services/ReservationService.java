package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.exception.ResourceNotFoundException;
import com.faridandaberk.carrental.model.*;
import com.faridandaberk.carrental.repository.*;
import com.faridandaberk.carrental.struct.ReservationResponseStruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
        // Validate input parameters
        if (dayCount <= 0) {
            throw new IllegalArgumentException("Day count must be positive");
        }

        // Find and validate car
        Car car = carRepository.findByBarcode(carBarcode)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with barcode: " + carBarcode));

        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new IllegalStateException("Car is not available for reservation");
        }

        // Find and validate member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));

        // Find and validate locations
        Location pickupLocation = locationRepository.findByCode(pickupLocationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Pickup location not found with code: " + pickupLocationCode));

        Location dropoffLocation = locationRepository.findByCode(dropoffLocationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Dropoff location not found with code: " + dropoffLocationCode));

        // Process equipment if provided
        List<Equipment> equipmentList = new ArrayList<>();
        if (equipmentCodes != null && !equipmentCodes.isEmpty()) {
            equipmentList = equipmentCodes.stream()
                    .map(code -> equipmentRepository.findByCode(code)
                            .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with code: " + code)))
                    .collect(Collectors.toList());
        }

        // Process services if provided
        List<com.faridandaberk.carrental.model.Service> serviceList = new ArrayList<>();
        if (serviceCodes != null && !serviceCodes.isEmpty()) {
            serviceList = serviceCodes.stream()
                    .map(code -> serviceRepository.findByCode(code)
                            .orElseThrow(() -> new ResourceNotFoundException("Service not found with code: " + code)))
                    .collect(Collectors.toList());
        }

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setMember(member);
        reservation.setPickUpLocation(pickupLocation);
        reservation.setDropOffLocation(dropoffLocation);
        reservation.setCreationDate(LocalDateTime.now());
        reservation.setPickUpDate(LocalDateTime.now().plusDays(1));
        reservation.setDropOffDate(reservation.getPickUpDate().plusDays(dayCount));
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setReservationNumber(generateReservationNumber());

        if (!equipmentList.isEmpty()) {
            reservation.setEquipment(new ArrayList<>(equipmentList));
        }
        if (!serviceList.isEmpty()) {
            reservation.setServices(new ArrayList<>(serviceList));
        }

        // Save reservation
        Reservation savedReservation = reservationRepository.save(reservation);

        // Update car status
        car.setStatus(CarStatus.LOANED);
        carRepository.save(car);
        carRepository.flush(); // Ensure car status is updated

        // Calculate total cost
        double totalCost = calculateTotalCost(car, dayCount, equipmentList, serviceList);

        return new ReservationResponseStruct(
                savedReservation.getReservationNumber(),
                pickupLocation.getName(),
                dropoffLocation.getName(),
                totalCost
        );
    }

    @Transactional
    public boolean cancelReservation(String reservationNumber) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with number: " + reservationNumber));

        reservation.setStatus(ReservationStatus.CANCELLED);

        Car car = reservation.getCar();
        if (car != null) {
            car.setStatus(CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        reservationRepository.save(reservation);
        carRepository.flush();
        return true;
    }

    @Transactional
    public boolean returnCar(String reservationNumber) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with number: " + reservationNumber));

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservation.setReturnDate(LocalDateTime.now());

        Car car = reservation.getCar();
        if (car != null) {
            car.setStatus(CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        reservationRepository.save(reservation);
        carRepository.flush();
        return true;
    }

    @Transactional
    public boolean deleteReservation(String reservationNumber) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with number: " + reservationNumber));

        // Update car status to AVAILABLE before deleting
        Car car = reservation.getCar();
        if (car != null) {
            car.setStatus(CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        // Disassociate relationships before deletion
        if (reservation.getMember() != null) {
            reservation.getMember().getReservations().remove(reservation);
        }
        reservation.setMember(null);
        reservation.setCar(null);
        reservation.getEquipment().clear();
        reservation.getServices().clear();

        reservationRepository.delete(reservation);
        carRepository.flush();
        return true;
    }

    public boolean addServiceToReservation(String reservationNumber, String serviceCode) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with number: " + reservationNumber));

        com.faridandaberk.carrental.model.Service service = serviceRepository.findByCode(serviceCode)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with code: " + serviceCode));

        if (reservation.getServices().contains(service)) {
            throw new IllegalStateException("Service is already added to this reservation");
        }

        reservation.getServices().add(service);
        reservationRepository.save(reservation);
        return true;
    }

    public boolean addEquipmentToReservation(String reservationNumber, String equipmentCode) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with number: " + reservationNumber));

        Equipment equipment = equipmentRepository.findByCode(equipmentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with code: " + equipmentCode));

        if (reservation.getEquipment().contains(equipment)) {
            throw new IllegalStateException("Equipment is already added to this reservation");
        }

        reservation.getEquipment().add(equipment);
        reservationRepository.save(reservation);
        return true;
    }

    private double calculateTotalCost(Car car, int dayCount, List<Equipment> equipment,
                                      List<com.faridandaberk.carrental.model.Service> services) {
        double totalCost = car.getDailyPrice() * dayCount;

        if (equipment != null) {
            totalCost += equipment.stream()
                    .mapToDouble(Equipment::getPrice)
                    .sum();
        }

        if (services != null) {
            totalCost += services.stream()
                    .mapToDouble(com.faridandaberk.carrental.model.Service::getPrice)
                    .sum();
        }

        return totalCost;
    }

    private String generateReservationNumber() {
        long count = reservationRepository.count() + 1;
        return String.format("%08d", count);
    }
}