package com.faridandaberk.carrental.controller;

import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;
import com.faridandaberk.carrental.services.CarService;
import com.faridandaberk.carrental.struct.CarStruct;
import com.faridandaberk.carrental.struct.RentedCarStruct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Tag(name = "Car Management", description = "APIs for managing cars in the rental system")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Operation(
            summary = "Search for available cars",
            description = "Search for available cars by car type and transmission type"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cars found successfully",
                    content = @Content(schema = @Schema(implementation = CarStruct.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No available cars found"
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<CarStruct>> searchAvailableCars(
            @Parameter(description = "Type of the car (e.g., STANDARD, SUV)", required = true)
            @RequestParam CarType carType,
            @Parameter(description = "Type of transmission (e.g., AUTOMATIC, MANUAL)", required = true)
            @RequestParam TransmissionType transmissionType) {
        List<CarStruct> cars = carService.searchAvailableCars(carType, transmissionType);
        return cars.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cars);
    }

    @Operation(
            summary = "Get all rented cars",
            description = "Retrieve a list of all currently rented cars"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Rented cars found successfully",
                    content = @Content(schema = @Schema(implementation = RentedCarStruct.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No rented cars found"
            )
    })
    @GetMapping("/rented")
    public ResponseEntity<List<RentedCarStruct>> getRentedCars() {
        List<RentedCarStruct> cars = carService.getAllRentedCars();
        return cars.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cars);
    }

    @Operation(
            summary = "Delete a car",
            description = "Delete a car by its barcode. Can only delete cars that are available and have no reservation history"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Car deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Car not found"
            ),
            @ApiResponse(
                    responseCode = "406",
                    description = "Car cannot be deleted (not available or has reservation history)"
            )
    })
    @DeleteMapping("/{barcode}")
    public ResponseEntity<Boolean> deleteCar(@PathVariable String barcode) {
        HttpStatus status = carService.deleteCar(barcode) ? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;
        return ResponseEntity.status(status).body(status == HttpStatus.OK);
    }

    @Operation(
            summary = "Add a new car",
            description = "Add a new car to the rental system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Car added successfully",
                    content = @Content(schema = @Schema(implementation = CarStruct.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid car data provided"
            )
    })
    @PostMapping
    public ResponseEntity<CarStruct> addCar(
            @Parameter(description = "Car details", required = true)
            @RequestBody CarStruct carDTO) {
        return ResponseEntity.ok(carService.addCar(carDTO));
    }
}