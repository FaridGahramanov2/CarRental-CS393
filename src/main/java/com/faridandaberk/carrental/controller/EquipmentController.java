package com.faridandaberk.carrental.controller;

import com.faridandaberk.carrental.services.EquipmentService;
import com.faridandaberk.carrental.struct.EquipmentStruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public ResponseEntity<EquipmentStruct> addEquipment(@RequestBody EquipmentStruct equipmentDTO) {
        return ResponseEntity.ok(equipmentService.addEquipment(equipmentDTO));
    }
}