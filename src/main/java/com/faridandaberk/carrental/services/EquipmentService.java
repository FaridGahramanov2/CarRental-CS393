package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.exception.ResourceNotFoundException;
import com.faridandaberk.carrental.model.Equipment;
import com.faridandaberk.carrental.repository.EquipmentRepository;
import com.faridandaberk.carrental.struct.EquipmentStruct;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public EquipmentStruct addEquipment(EquipmentStruct equipmentDTO) {
        // Validate input
        if (equipmentDTO == null) {
            throw new IllegalArgumentException("Equipment data cannot be null");
        }

        if (equipmentDTO.code() == null || equipmentDTO.code().trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment code cannot be empty");
        }

        if (equipmentDTO.price() == null || equipmentDTO.price() < 0) {
            throw new IllegalArgumentException("Equipment price must be non-negative");
        }

        // Check if equipment with same code already exists
        if (equipmentRepository.findByCode(equipmentDTO.code()).isPresent()) {
            throw new IllegalStateException("Equipment with this code already exists");
        }

        Equipment equipment = new Equipment(
                null,
                equipmentDTO.code(),
                equipmentDTO.name(),
                equipmentDTO.price(),
                new HashSet<>()
        );
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return mapToDTO(savedEquipment);
    }

    public EquipmentStruct getEquipmentByCode(String code) {
        Equipment equipment = equipmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with code: " + code));
        return mapToDTO(equipment);
    }

    public List<EquipmentStruct> getAllEquipment() {
        List<Equipment> equipmentList = equipmentRepository.findAll();
        if (equipmentList.isEmpty()) {
            throw new ResourceNotFoundException("No equipment found");
        }
        return equipmentList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public boolean deleteEquipment(String code) {
        Equipment equipment = equipmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with code: " + code));

        // Check if equipment is being used in any reservations
        if (!equipment.getReservations().isEmpty()) {
            throw new IllegalStateException("Equipment cannot be deleted as it is associated with reservations");
        }

        equipmentRepository.delete(equipment);
        return true;
    }

    public EquipmentStruct updateEquipment(String code, EquipmentStruct equipmentDTO) {
        Equipment equipment = equipmentRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with code: " + code));

        if (equipmentDTO.name() != null) {
            equipment.setName(equipmentDTO.name());
        }
        if (equipmentDTO.price() != null) {
            if (equipmentDTO.price() < 0) {
                throw new IllegalArgumentException("Equipment price must be non-negative");
            }
            equipment.setPrice(equipmentDTO.price());
        }

        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return mapToDTO(updatedEquipment);
    }

    private EquipmentStruct mapToDTO(Equipment equipment) {
        return new EquipmentStruct(
                equipment.getCode(),
                equipment.getName(),
                equipment.getPrice()
        );
    }
}