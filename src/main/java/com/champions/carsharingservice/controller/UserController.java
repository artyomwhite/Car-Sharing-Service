package com.champions.carsharingservice.controller;

import com.champions.carsharingservice.dto.UserInfoResponseDto;
import com.champions.carsharingservice.dto.UserResponseDto;
import com.champions.carsharingservice.dto.UserUpdateRequestDto;
import com.champions.carsharingservice.dto.UserUpdateRoleRequestDto;
import com.champions.carsharingservice.model.User;
import com.champions.carsharingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Tag(name = "User Management",
        description = "Endpoints for updating and getting user info")
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/role")
    @Operation(summary = "Update user's role",
                description = """
                        Manager can update user's role.
                        Params: new role""")
    public UserResponseDto updateUserRole(@PathVariable @Positive Long id,
                                          @RequestBody UserUpdateRoleRequestDto newRole) {
        return userService.updateUserRole(id, newRole.role());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/me")
    @Operation(summary = "Get info about user",
            description = "Get user's firstname, lastname and email by user's id")
    public UserInfoResponseDto getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getUserInfo(user.getId());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PatchMapping("/update")
    @Operation(summary = "Update user info",
            description = "Update user's firstName and lastName by user's id")
    public UserInfoResponseDto updateUserById(Authentication authentication,
                                               @RequestBody UserUpdateRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updateUserById(user.getId(), requestDto);
    }
}
