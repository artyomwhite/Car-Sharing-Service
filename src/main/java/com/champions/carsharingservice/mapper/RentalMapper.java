package com.champions.carsharingservice.mapper;

import com.champions.carsharingservice.config.MapperConfig;
import com.champions.carsharingservice.dto.rental.RentalDto;
import com.champions.carsharingservice.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    @Mapping(source = "car.id", target = "carId")
    @Mapping(source = "car.brand", target = "carBrand")
    @Mapping(source = "car.model", target = "carModel")
    RentalDto toDto(Rental rental);
}
