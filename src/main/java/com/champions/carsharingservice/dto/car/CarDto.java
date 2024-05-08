package com.champions.carsharingservice.dto.car;

import com.champions.carsharingservice.model.Car;
import java.math.BigDecimal;

public record CarDto(
        Long id,
        String model,
        String brand,
        Car.CarType type,
        Integer inventory,
        BigDecimal dailyFee) {
}
