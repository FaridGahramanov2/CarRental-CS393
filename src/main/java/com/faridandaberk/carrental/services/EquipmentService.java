package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.model.Equipment;
import com.faridandaberk.carrental.repository.EquipmentRepository;
import com.faridandaberk.carrental.struct.EquipmentStruct;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public EquipmentStruct addEquipment(EquipmentStruct equipmentDTO) {
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

    private EquipmentStruct mapToDTO(Equipment equipment) {
        return new EquipmentStruct(
                equipment.getCode(),
                equipment.getName(),
                equipment.getPrice()
        );
    }
}