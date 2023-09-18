package com.springframework.boot.onlinebookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateShoppingCartRequestDto {
    @NotNull
    @Min(value = 1)
    private Long bookId;
    @NotNull
    @Min(value = 1)
    private int quantity;
}
