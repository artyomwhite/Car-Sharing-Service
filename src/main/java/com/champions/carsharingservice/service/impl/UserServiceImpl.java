package com.champions.carsharingservice.service.impl;

import com.champions.carsharingservice.dto.UserInfoResponseDto;
import com.champions.carsharingservice.dto.UserRegistrationRequestDto;
import com.champions.carsharingservice.dto.UserResponseDto;
import com.champions.carsharingservice.dto.UserUpdateRequestDto;
import com.champions.carsharingservice.exception.EntityNotFoundException;
import com.champions.carsharingservice.exception.RegistrationException;
import com.champions.carsharingservice.mapper.UserMapper;
import com.champions.carsharingservice.model.Role;
import com.champions.carsharingservice.model.User;
import com.champions.carsharingservice.repository.RoleRepository;
import com.champions.carsharingservice.repository.UserRepository;
import com.champions.carsharingservice.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("Unable to complete registration. User already exists");
        }
        Role defaultRole = roleRepository.findRoleByName(Role.RoleName.CUSTOMER).orElseThrow();
        User user = userMapper.toUser(requestDto);
        user.setRoles(Set.of(defaultRole));
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserResponseDto updateUserRole(Long id, String newRole) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no user with id: " + id)
        );
        Role roleByName = roleRepository.findRoleByName(Role.RoleName.valueOf(newRole))
                .orElseThrow(
                        () -> new EntityNotFoundException("There is no role with the name: "
                                + newRole)
                );
        user.getRoles().add(roleByName);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserInfoResponseDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no user with id: " + id)
        );
        return userMapper.toUserInfo(user);
    }

    @Override
    public UserInfoResponseDto updateUserById(Long id, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no user with id: " + id)
        );
        userMapper.toUserUpdate(requestDto, user);
        User updateUser = userRepository.save(user);
        return userMapper.toUserInfo(updateUser);
    }
}
