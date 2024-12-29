package com.faridandaberk.carrental.struct;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ReservationRequestDTO(
        @NotBlank(message = "Car barcode is required")
        String carBarcode,

        @NotNull(message = "Day count is required")
        @Min(value = 1, message = "Day count must be at least 1")
        Integer dayCount,

        @NotNull(message = "Member ID is required")
        Long memberId,

        @NotBlank(message = "Pickup location code is required")
        String pickupLocationCode,

        @NotBlank(message = "Dropoff location code is required")
        String dropoffLocationCode,

        List<String> equipmentCodes,

        List<String> serviceCodes
) {}