package com.springframework.boot.onlinebookstore.dto.user;

import com.springframework.boot.onlinebookstore.validation.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        @Size(min = 4, max = 50)
        String email,
        @NotBlank
        @Size(min = 4, max = 100)
        String password
) {
}
