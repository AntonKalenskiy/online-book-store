package com.springframework.boot.onlinebookstore.service;

import com.springframework.boot.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.springframework.boot.onlinebookstore.dto.user.UserResponseDto;
import com.springframework.boot.onlinebookstore.exception.RegistrationException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
