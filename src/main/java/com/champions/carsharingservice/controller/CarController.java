package com.champions.carsharingservice.controller;

import com.champions.carsharingservice.dto.car.CarDto;
import com.champions.carsharingservice.dto.car.CreateCarRequestDto;
import com.champions.carsharingservice.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car management",
        description = "Endpoints for browsing and managing cars depending on your role")
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    @Operation(summary = "Add a new car",
            description = """
                    Manager can add a new car,
                    Params: car model, brand, type, amount, daily fee""")
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CarDto add(@RequestBody @Valid CreateCarRequestDto request) {
        return carService.save(request);
    }

    @Operation(summary = "Get all cars",
            description = """
                    Everybody can get all cars
                    with parameters model, brand, type, amount, daily fee""")
    @GetMapping
    public List<CarDto> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return carService.getAll(pageable);
    }

    @Operation(summary = "Get a car by id",
            description = "Everybody can get a car by id, if the car with this id exists")
    @GetMapping("/{id}")
    public CarDto getById(@PathVariable @Positive Long id) {
        return carService.getById(id);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update a car",
            description = "Manager can update a car by id, if the car with this id exist")
    @PutMapping("/{id}")
    public CarDto update(@PathVariable @Positive Long id,
                         @RequestBody @Valid CreateCarRequestDto request) {
        return carService.update(id, request);
    }

    @Operation(summary = "Delete a car",
            description = "Manager can delete a car by id, if the car with this id exist")
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        carService.delete(id);
    }
}
