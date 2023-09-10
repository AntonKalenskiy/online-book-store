package com.springframework.boot.onlinebookstore.service;

import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {

    ShoppingCartDto findCart(Authentication authentication);

    ShoppingCartDto addBookToCart(CreateShoppingCartRequestDto shoppingCartRequestDto,
                                  Authentication authentication);

    ShoppingCartDto updateQuantity(Long cartItemId, CreateCartItemRequestDto requestDto);

    void deleteCartItemById(Long cartItemId);

}
