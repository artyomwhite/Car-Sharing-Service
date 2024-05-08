package com.champions.carsharingservice.service;

import com.champions.carsharingservice.dto.car.CarDto;
import com.champions.carsharingservice.dto.car.CreateCarRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CarService {
    CarDto save(CreateCarRequestDto request);

    List<CarDto> getAll(Pageable pageable);

    CarDto getById(Long id);

    CarDto update(Long id, CreateCarRequestDto request);

    void delete(Long id);
}
