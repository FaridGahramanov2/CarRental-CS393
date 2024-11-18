package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.struct.ReservationResponseStruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {


//    private final ReservationRepository reservationRepository;  ----ToDO waiting repo
//    private final CarRepository carRepository;    ----ToDO waiting repo
//    private final MemberRepository memberRepository;    ----ToDO waiting repo

    public ReservationResponseStruct makeReservation(
            String carBarcode,
            int dayCount,
            Long memberId,
            String pickupLocationCode,
            String dropoffLocationCode,
            List<String> equipmentCodes,
            List<String> serviceCodes) {
        return null;
    }

    public boolean addServiceToReservation(String reservationNumber, String serviceCode) {
        return false;
    }

    public boolean addEquipmentToReservation(String reservationNumber, String equipmentCode) {
        return false;
    }

    public boolean returnCar(String reservationNumber) {
        return false;
    }

    public boolean cancelReservation(String reservationNumber) {
        return false;
    }

    public boolean deleteReservation(String reservationNumber) {
        return false;
    }
}