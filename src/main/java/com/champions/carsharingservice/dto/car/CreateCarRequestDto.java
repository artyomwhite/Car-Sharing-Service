package com.champions.carsharingservice.dto.car;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateCarRequestDto(
        @NotBlank(message = "model can't be null")
        @Size(min = 1, max = 100, message = "model should be between 1 and 100 characters")
        String model,

        @NotBlank(message = "brand can't be null")
        @Size(min = 1, max = 100, message = "brand should be between 1 and 50 characters")
        String brand,

        @NotBlank(message = "type can't be null")
        @Size(min = 1, max = 50, message = "type should be between 1 and 50 characters")
        String type,

        @Min(value = 0, message = "amount available can't be less than 0")
        Integer inventory,

        @Min(value = 0, message = "daily fee can't be less than 0")
        BigDecimal dailyFee,

        String imageUrl
) {
}
