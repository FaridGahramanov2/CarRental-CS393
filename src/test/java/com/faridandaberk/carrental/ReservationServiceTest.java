package com.faridandaberk.carrental;

import com.faridandaberk.carrental.model.CarType;
import com.faridandaberk.carrental.model.TransmissionType;
import com.faridandaberk.carrental.services.*;
import com.faridandaberk.carrental.struct.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CarService carService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EquipmentService equipmentService;

    @Test
    void makeReservation_MakesReservation_ReturnsReservationResponse() {
        
        CarStruct carDTO = new CarStruct(
                "Toyota",
                "Corolla",
                "123ABC",
                CarType.STANDARD,
                TransmissionType.MANUAL,
                5,
                100.0,
                15000,
                "34TEST123"
        );
        carService.addCar(carDTO);


        MemberStruct memberDTO = new MemberStruct(
                "John Doe",
                "123 Main St",
                "test.user@example.com",  // Changed email
                "555-1234",
                "TEST_DL_123"
        );
        MemberStruct savedMember = memberService.registerMember(memberDTO);


        EquipmentStruct equipmentDTO = new EquipmentStruct(
                "TEST_EQ_001",
                "GPS",
                10.0
        );
        equipmentService.addEquipment(equipmentDTO);


        ReservationResponseStruct result = reservationService.makeReservation(
                "123ABC",
                7,
                1L,
                "1",
                "2",
                Arrays.asList("GPS"),
                Arrays.asList("Additional Driver")
        );


        assertThat(result).isNotNull();
        assertThat(result.reservationNumber()).isNotBlank();
        assertThat(result.pickupLocation()).isEqualTo("İstanbul Airport");
        assertThat(result.dropoffLocation()).isEqualTo("İstanbul Sabiha Gökçen Airport");
        assertThat(result.totalCost()).isGreaterThan(0.0);
    }
}