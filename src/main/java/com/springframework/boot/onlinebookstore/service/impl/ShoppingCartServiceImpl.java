package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.ShoppingCartMapper;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.springframework.boot.onlinebookstore.model.ShoppingCart;
import com.springframework.boot.onlinebookstore.model.User;
import com.springframework.boot.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.springframework.boot.onlinebookstore.repository.user.UserRepository;
import com.springframework.boot.onlinebookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto findCart(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User with such email doesn't exist")
        );
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addBookToCart(CreateShoppingCartRequestDto shoppingCartRequestDto,
                                         Authentication authentication) {
        return null;
    }

    @Override
    public ShoppingCartDto updateQuantity(Long cartItemId, CreateCartItemRequestDto requestDto) {
        return null;
    }

    @Override
    public void deleteCartItemById(Long cartItemId) {

    }
}
