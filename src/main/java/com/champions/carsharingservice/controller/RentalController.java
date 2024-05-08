package com.champions.carsharingservice.controller;

import com.champions.carsharingservice.dto.rental.CreateRentalRequestDto;
import com.champions.carsharingservice.dto.rental.RentalDto;
import com.champions.carsharingservice.model.User;
import com.champions.carsharingservice.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
@Tag(name = "Rental management",
        description = "Endpoints for managing and browsing rentals depending on you role")
public class RentalController {
    private final RentalService rentalService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    @Operation(summary = "Create new rental",
            description = """
            Create a new rental 
            Prams: carId, returnDateTime""")
    public RentalDto createRental(@RequestBody CreateRentalRequestDto requestDto,
                                  Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return rentalService.createRental(requestDto, user.getId());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    @Operation(summary = "Get all rentals",
            description = """
                    Get all rentals for user,
                    Pageable default: page = 0, size = 10""")
    public List<RentalDto> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                  Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return rentalService.getAllRentals(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/")
    @Operation(summary = "Get all rentals by activeness",
            description = """
                    Get all user's rentals by their activeness 
                    (if actual return date is null -> active)
                    Pageable default: page = 0, size = 10
                    """)
    public List<RentalDto> getAllRentalsByActiveness(@RequestParam(name = "is_active")
                                                     boolean isActive,
                                                     Authentication authentication,
                                                     @PageableDefault(page = 0, size = 10)
                                                     Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        if (isActive) {
            return rentalService.getAllActiveRentals(user.getId(), pageable);
        } else {
            return rentalService.getAllNotActiveRentals(user.getId(), pageable);
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/search/")
    @Operation(summary = "Search rentals for manager",
            description = """
                    Search rentals using userId and activeness, 
                    default activeness = true
                    default: page = 0, size = 10
                    """)
    public List<RentalDto> getAllRentalsByUserAndActiveness(
            @RequestParam(name = "user_id")
            Long userId,
            @RequestParam(name = "is_active", defaultValue = "true")
            boolean isActive,
            @PageableDefault(page = 0, size = 10)
            Pageable pageable) {
        if (isActive) {
            return rentalService.getAllActiveRentals(userId, pageable);
        } else {
            return rentalService.getAllNotActiveRentals(userId, pageable);
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{id}/return")
    @Operation(summary = "Return rental by id",
            description = """
            Return rental by setting actual return date
            if actual return date is after return date, new payment(Fine) is created
            depending on how late the return was
            """)
    public RentalDto returnRental(@PathVariable @Positive Long id) {
        return rentalService.setActualReturnDate(id);
    }
}
