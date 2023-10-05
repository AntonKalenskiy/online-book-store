package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.mapper.UserMapper;
import com.springframework.boot.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.springframework.boot.onlinebookstore.dto.user.UserResponseDto;
import com.springframework.boot.onlinebookstore.exception.RegistrationException;
import com.springframework.boot.onlinebookstore.model.Role;
import com.springframework.boot.onlinebookstore.model.User;
import com.springframework.boot.onlinebookstore.repository.role.RoleRepository;
import com.springframework.boot.onlinebookstore.repository.user.UserRepository;
import com.springframework.boot.onlinebookstore.service.UserService;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration. "
                    + "User with such email already exists.");
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName(Role.RoleName.ROLE_USER)));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User with such email doesn't exist")
        );
    }
}
