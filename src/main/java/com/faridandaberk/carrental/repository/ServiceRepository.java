package com.faridandaberk.carrental.repository;

import com.faridandaberk.carrental.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByNameIn(List<String> names);
    Optional<Service> findByCode(String code);
}