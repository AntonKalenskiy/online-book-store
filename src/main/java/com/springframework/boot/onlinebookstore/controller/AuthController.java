package com.springframework.boot.onlinebookstore.controller;

import com.springframework.boot.onlinebookstore.dto.user.UserLoginRequestDto;
import com.springframework.boot.onlinebookstore.dto.user.UserLoginResponseDto;
import com.springframework.boot.onlinebookstore.dto.user.UserRegistrationRequestDto;
import com.springframework.boot.onlinebookstore.dto.user.UserResponseDto;
import com.springframework.boot.onlinebookstore.exception.RegistrationException;
import com.springframework.boot.onlinebookstore.security.AuthenticationService;
import com.springframework.boot.onlinebookstore.security.JwtAuthenticationFilter;
import com.springframework.boot.onlinebookstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
      return authenticationService.authenticate(requestDto);
    }
}
