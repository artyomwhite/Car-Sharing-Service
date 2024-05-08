package com.champions.carsharingservice.mapper;

import com.champions.carsharingservice.config.MapperConfig;
import com.champions.carsharingservice.dto.PaymentDto;
import com.champions.carsharingservice.model.Payment;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
}
