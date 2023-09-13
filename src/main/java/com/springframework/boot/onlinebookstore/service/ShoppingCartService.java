package com.springframework.boot.onlinebookstore.service;

import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.springframework.boot.onlinebookstore.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface ShoppingCartService {

    ShoppingCartDto findCart(User user);

    ShoppingCartDto addBookToCart(CreateShoppingCartRequestDto shoppingCartRequestDto,
                                  User user);

    ShoppingCartDto updateBookQuantity(Long cartItemId,
                                       CreateCartItemRequestDto requestDto,
                                       User user);

    void deleteCartItemById(Long cartItemId, User user);

}
