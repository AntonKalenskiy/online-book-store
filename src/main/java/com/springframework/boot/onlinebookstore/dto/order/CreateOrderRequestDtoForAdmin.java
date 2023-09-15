package com.springframework.boot.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequestDtoForAdmin {
    @NotNull
    @NotBlank
    private String status;
}
