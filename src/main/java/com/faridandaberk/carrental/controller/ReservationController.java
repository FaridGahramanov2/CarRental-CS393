package com.faridandaberk.carrental.controller;

import com.faridandaberk.carrental.services.ReservationService;
import com.faridandaberk.carrental.struct.ReservationResponseStruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseStruct> makeReservation(
            @RequestParam String carBarcode,
            @RequestParam int dayCount,
            @RequestParam Long memberId,
            @RequestParam String pickupLocationCode,
            @RequestParam String dropoffLocationCode,
            @RequestParam(required = false) List<String> equipmentCodes,
            @RequestParam(required = false) List<String> serviceCodes) {
        ReservationResponseStruct response = reservationService.makeReservation(
                carBarcode, dayCount, memberId, pickupLocationCode,
                dropoffLocationCode, equipmentCodes, serviceCodes);
        return response != null ? ResponseEntity.ok(response) :
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PostMapping("/{reservationNumber}/services/{serviceCode}")
    public ResponseEntity<Boolean> addService(
            @PathVariable String reservationNumber,
            @PathVariable String serviceCode) {
        return reservationService.addServiceToReservation(reservationNumber, serviceCode) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/{reservationNumber}/equipment/{equipmentCode}")
    public ResponseEntity<Boolean> addEquipment(
            @PathVariable String reservationNumber,
            @PathVariable String equipmentCode) {
        return reservationService.addEquipmentToReservation(reservationNumber, equipmentCode) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/{reservationNumber}/return")
    public ResponseEntity<Boolean> returnCar(@PathVariable String reservationNumber) {
        return reservationService.returnCar(reservationNumber) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/{reservationNumber}/cancel")
    public ResponseEntity<Boolean> cancelReservation(@PathVariable String reservationNumber) {
        return reservationService.cancelReservation(reservationNumber) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{reservationNumber}")
    public ResponseEntity<Boolean> deleteReservation(@PathVariable String reservationNumber) {
        return reservationService.deleteReservation(reservationNumber) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }
}