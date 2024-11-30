package com.faridandaberk.carrental.repository;

import com.faridandaberk.carrental.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByNameIn(List<String> names);
    Optional<Equipment> findByCode(String code);
}