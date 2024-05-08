package com.champions.carsharingservice.mapper;

import com.champions.carsharingservice.config.MapperConfig;
import com.champions.carsharingservice.dto.car.CarDto;
import com.champions.carsharingservice.dto.car.CreateCarRequestDto;
import com.champions.carsharingservice.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CarMapper {

    Car toEntity(CreateCarRequestDto request);

    CarDto toDto(Car car);

    void updateCar(CreateCarRequestDto dto, @MappingTarget Car car);
}
