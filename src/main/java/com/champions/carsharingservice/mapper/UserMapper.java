package com.champions.carsharingservice.mapper;

import com.champions.carsharingservice.config.MapperConfig;
import com.champions.carsharingservice.dto.UserInfoResponseDto;
import com.champions.carsharingservice.dto.UserRegistrationRequestDto;
import com.champions.carsharingservice.dto.UserResponseDto;
import com.champions.carsharingservice.dto.UserUpdateRequestDto;
import com.champions.carsharingservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toUser(UserRegistrationRequestDto request);

    UserInfoResponseDto toUserInfo(User user);

    void toUserUpdate(UserUpdateRequestDto requestDto, @MappingTarget User user);
}
