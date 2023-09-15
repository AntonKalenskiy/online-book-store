package com.springframework.boot.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequestDtoForUser {
    @NotNull
    @NotBlank
    private String shippingAddress;
}
