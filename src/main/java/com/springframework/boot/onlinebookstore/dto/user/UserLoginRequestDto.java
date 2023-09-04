package com.springframework.boot.onlinebookstore.dto.user;

import com.springframework.boot.onlinebookstore.validation.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank
    @Email
    @Size(min = 4, max = 50)
    private String email;
    @NotBlank
    @Size(min = 4, max = 100)
    private String password;
}
