package com.faridandaberk.carrental.controller;

import com.faridandaberk.carrental.exception.ResourceNotFoundException;
import com.faridandaberk.carrental.services.ReservationService;
import com.faridandaberk.carrental.struct.ReservationRequestDTO;
import com.faridandaberk.carrental.struct.ReservationResponseStruct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Manage reservations for car rentals")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @Operation(
            summary = "Make a reservation",
            description = "Create a new reservation for a car rental",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reservation created successfully",
                            content = @Content(schema = @Schema(implementation = ReservationResponseStruct.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "404", description = "Car, member, or location not found"),
                    @ApiResponse(responseCode = "406", description = "Car is not available")
            }
    )
    public ResponseEntity<ReservationResponseStruct> makeReservation(@Valid @RequestBody ReservationRequestDTO request) {
        try {
            ReservationResponseStruct response = reservationService.makeReservation(
                    request.carBarcode(),
                    request.dayCount(),
                    request.memberId(),
                    request.pickupLocationCode(),
                    request.dropoffLocationCode(),
                    request.equipmentCodes(),
                    request.serviceCodes());
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Add a service to a reservation",
            description = "Attach a service to an existing reservation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Service added successfully"),
                    @ApiResponse(responseCode = "404", description = "Reservation or service not found")
            }
    )
    @PostMapping("/{reservationNumber}/services/{serviceCode}")
    public ResponseEntity<Boolean> addService(@PathVariable String reservationNumber, @PathVariable String serviceCode) {
        try {
            return reservationService.addServiceToReservation(reservationNumber, serviceCode) ?
                    ResponseEntity.ok(true) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Add equipment to a reservation",
            description = "Attach equipment to an existing reservation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Equipment added successfully"),
                    @ApiResponse(responseCode = "404", description = "Reservation or equipment not found")
            }
    )
    @PostMapping("/{reservationNumber}/equipment/{equipmentCode}")
    public ResponseEntity<Boolean> addEquipment(@PathVariable String reservationNumber, @PathVariable String equipmentCode) {
        try {
            return reservationService.addEquipmentToReservation(reservationNumber, equipmentCode) ?
                    ResponseEntity.ok(true) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Return a car",
            description = "Mark a car as returned for a reservation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Car returned successfully"),
                    @ApiResponse(responseCode = "404", description = "Reservation not found")
            }
    )
    @PostMapping("/{reservationNumber}/return")
    public ResponseEntity<Boolean> returnCar(@PathVariable String reservationNumber) {
        return reservationService.returnCar(reservationNumber) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Cancel a reservation",
            description = "Cancel an existing reservation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reservation canceled successfully"),
                    @ApiResponse(responseCode = "404", description = "Reservation not found")
            }
    )
    @PostMapping("/{reservationNumber}/cancel")
    public ResponseEntity<Boolean> cancelReservation(@PathVariable String reservationNumber) {
        return reservationService.cancelReservation(reservationNumber) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Delete a reservation",
            description = "Remove an existing reservation permanently",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reservation deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Reservation not found")
            }
    )
    @DeleteMapping("/{reservationNumber}")
    public ResponseEntity<Boolean> deleteReservation(@PathVariable String reservationNumber) {
        return reservationService.deleteReservation(reservationNumber) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }
}