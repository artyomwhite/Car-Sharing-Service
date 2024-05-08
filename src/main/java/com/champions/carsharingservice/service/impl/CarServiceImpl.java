package com.champions.carsharingservice.service.impl;

import com.champions.carsharingservice.dto.car.CarDto;
import com.champions.carsharingservice.dto.car.CreateCarRequestDto;
import com.champions.carsharingservice.exception.EntityNotFoundException;
import com.champions.carsharingservice.mapper.CarMapper;
import com.champions.carsharingservice.model.Car;
import com.champions.carsharingservice.repository.CarRepository;
import com.champions.carsharingservice.service.CarService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private static final String DEFAULT_IMAGE_URL = "https://i.ibb.co/N6JDyLJ/car.png";
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto save(CreateCarRequestDto request) {
        Car car = carMapper.toEntity(request);
        if (request.imageUrl() == null) {
            car.setImageUrl(DEFAULT_IMAGE_URL);
        }
        car.setType(Car.CarType.valueOf(request.type()));
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public List<CarDto> getAll(Pageable pageable) {
        return carRepository.findAll(pageable).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarDto getById(Long id) {
        return carMapper.toDto(carById(id));
    }

    @Override
    @Transactional
    public CarDto update(Long id, CreateCarRequestDto request) {
        Car carToUpdate = carById(id);
        carMapper.updateCar(request, carToUpdate);
        if (request.imageUrl() == null) {
            carToUpdate.setImageUrl(DEFAULT_IMAGE_URL);
        }
        return carMapper.toDto(carToUpdate);
    }

    @Override
    public void delete(Long id) {
        if (!carRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find car by id: " + id);
        }
        carRepository.deleteById(id);
    }

    private Car carById(Long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find car by id: " + id)
        );
    }
}
