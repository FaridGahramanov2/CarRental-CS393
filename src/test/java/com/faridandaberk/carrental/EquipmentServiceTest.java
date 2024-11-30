package com.faridandaberk.carrental;

import com.faridandaberk.carrental.services.EquipmentService;
import com.faridandaberk.carrental.struct.EquipmentStruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EquipmentServiceTest {

    @Autowired
    private EquipmentService equipmentService;

    @Test
    void addEquipment_AddsEquipment_ReturnsEquipmentDTO() {
        // Arrange
        EquipmentStruct equipmentDTO = new EquipmentStruct(
                "TEST_EQ_001", // Using a different code from the DataInitializer
                "Test GPS",
                10.0
        );

        // Act
        EquipmentStruct result = equipmentService.addEquipment(equipmentDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.code()).isEqualTo("TEST_EQ_001");
        assertThat(result.name()).isEqualTo("Test GPS");
        assertThat(result.price()).isEqualTo(10.0);
    }
}