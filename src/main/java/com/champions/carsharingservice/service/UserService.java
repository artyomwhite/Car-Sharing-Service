package com.champions.carsharingservice.service;

import com.champions.carsharingservice.dto.UserInfoResponseDto;
import com.champions.carsharingservice.dto.UserRegistrationRequestDto;
import com.champions.carsharingservice.dto.UserResponseDto;
import com.champions.carsharingservice.dto.UserUpdateRequestDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    UserResponseDto updateUserRole(Long id, String newRole);

    UserInfoResponseDto getUserInfo(Long id);

    UserInfoResponseDto updateUserById(Long id, UserUpdateRequestDto requestDto);
}
