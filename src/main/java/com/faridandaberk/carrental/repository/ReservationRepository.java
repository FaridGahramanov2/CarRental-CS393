package com.faridandaberk.carrental.repository;

import com.faridandaberk.carrental.model.Car;
import com.faridandaberk.carrental.model.Reservation;
import com.faridandaberk.carrental.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationNumber(String reservationNumber);
    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByCar(Car car);
}