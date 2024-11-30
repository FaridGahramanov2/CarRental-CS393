package com.faridandaberk.carrental.repository;

import com.faridandaberk.carrental.model.Car;
import com.faridandaberk.carrental.model.CarStatus;
import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {



    Optional<Car> findByBarcode(String barcode);
    List<Car> findByCarTypeAndTransmissionTypeAndStatus(
            CarType carType,
            TransmissionType transmissionType,
            CarStatus status
    );

    List<Car> findByStatusIn(Collection<CarStatus> statuses);




}

