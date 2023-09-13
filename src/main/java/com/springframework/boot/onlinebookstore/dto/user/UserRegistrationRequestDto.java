package com.springframework.boot.onlinebookstore.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springframework.boot.onlinebookstore.validation.Email;
import com.springframework.boot.onlinebookstore.validation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldMatch(first = "password", second = "repeatPassword",
        message = "Password and repeatPassword must be the same")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    @Size(min = 4, max = 50)
    private String email;
    @NotBlank
    @Size(min = 4, max = 100)
    private String password;
    @NotBlank
    @Size(min = 4, max = 100)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String shippingAddress;
}
