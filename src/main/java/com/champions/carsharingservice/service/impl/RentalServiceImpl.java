package com.champions.carsharingservice.service.impl;

import com.champions.carsharingservice.dto.rental.CreateRentalRequestDto;
import com.champions.carsharingservice.dto.rental.RentalDto;
import com.champions.carsharingservice.exception.EntityNotFoundException;
import com.champions.carsharingservice.exception.NoCarsAvailableException;
import com.champions.carsharingservice.exception.RentalNotActiveException;
import com.champions.carsharingservice.mapper.RentalMapper;
import com.champions.carsharingservice.model.Car;
import com.champions.carsharingservice.model.Rental;
import com.champions.carsharingservice.repository.CarRepository;
import com.champions.carsharingservice.repository.RentalRepository;
import com.champions.carsharingservice.repository.UserRepository;
import com.champions.carsharingservice.service.NotificationService;
import com.champions.carsharingservice.service.RentalService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public RentalDto createRental(CreateRentalRequestDto requestDto, Long userId) {
        Car car = carRepository.findById(requestDto.carId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find car by id "
                        + requestDto.carId()));
        if (car.getInventory() < 1) {
            throw new NoCarsAvailableException("No available cars left");
        }
        car.setInventory(car.getInventory() - 1);
        Rental rental = new Rental();
        rental.setCar(car);
        rental.setUser(userRepository.getReferenceById(userId));
        rental.setRentalDateTime(LocalDateTime.now());
        rental.setReturnDateTime(requestDto.returnDateTime());
        // Вираховується ціна залежно від дати
        notificationService.sendMessageAboutCreatedRental(rental);
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    @Override
    public List<RentalDto> getAllRentals(Long userId, Pageable pageable) {
        return rentalRepository.getAllByUserId(userId, pageable).stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    public List<RentalDto> getAllActiveRentals(Long userId, Pageable pageable) {
        return rentalRepository.getAllByUserIdAndActualReturnDateTimeIsNull(userId, pageable)
                .stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    public List<RentalDto> getAllNotActiveRentals(Long userId, Pageable pageable) {
        return rentalRepository.getAllByUserIdAndActualReturnDateTimeIsNotNull(userId, pageable)
                .stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RentalDto setActualReturnDate(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find rental by id "
                        + rentalId));
        if (rental.getActualReturnDateTime() != null) {
            throw new RentalNotActiveException("This rental has already been returned");
        }
        rental.setActualReturnDateTime(LocalDateTime.now());
        if (rental.getActualReturnDateTime().isAfter(rental.getReturnDateTime())) {
            notificationService.sendMessageAboutOverdueRental(rental);
            // додається оплата FINE
            // rental.getPayments().add();
            System.out.println("Fine");
        }
        Car car = rental.getCar();
        car.setInventory(car.getInventory() + 1);
        return rentalMapper.toDto(rental);
    }
}
