package com.champions.carsharingservice.controller;

import com.champions.carsharingservice.dto.UserLoginRequestDto;
import com.champions.carsharingservice.dto.UserLoginResponseDto;
import com.champions.carsharingservice.dto.UserRegistrationRequestDto;
import com.champions.carsharingservice.dto.UserResponseDto;
import com.champions.carsharingservice.security.AuthenticationService;
import com.champions.carsharingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication controller",
        description = "Endpoints for login and registration")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register",
            description = "Register new user by email, first name, last name and password")
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.register(request);
    }

    @Operation(summary = "Login",
            description = "Login user by email and password")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
