package com.faridandaberk.carrental.controller;

import com.faridandaberk.carrental.services.EquipmentService;
import com.faridandaberk.carrental.struct.EquipmentStruct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
@Tag(
        name = "Equipment Management",
        description = "APIs for managing additional equipment for car rentals (e.g., GPS, child seats, etc.)"
)
public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @Operation(
            summary = "Add new equipment",
            description = "Add a new piece of equipment to the rental system. Each equipment must have a unique code."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment added successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EquipmentStruct.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid equipment data provided",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Equipment with this code already exists",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<EquipmentStruct> addEquipment(@RequestBody EquipmentStruct equipmentDTO) {
        try {
            return ResponseEntity.ok(equipmentService.addEquipment(equipmentDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}