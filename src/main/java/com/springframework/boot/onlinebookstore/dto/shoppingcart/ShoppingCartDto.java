package com.springframework.boot.onlinebookstore.dto.shoppingcart;

import com.springframework.boot.onlinebookstore.dto.cartitem.CartItemDto;
import java.util.List;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
}
