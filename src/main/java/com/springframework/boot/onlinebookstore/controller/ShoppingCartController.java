package com.springframework.boot.onlinebookstore.controller;

import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.springframework.boot.onlinebookstore.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getCart(Authentication authentication) {
        return shoppingCartService.findCart(authentication);
    }

    @PostMapping
    public ShoppingCartDto addBookToCart(
            @RequestBody @Valid CreateShoppingCartRequestDto shoppingCartRequestDto,
            Authentication authentication) {
        return null;
    }

    @PutMapping("cart-items/{cartItemId}")
    public ShoppingCartDto updateQuantityOfBooks(@PathVariable Long cartItemId,
                                          @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return null;
    }

    @DeleteMapping("cart-items/{cartItemId}")
    public void deleteBook(@PathVariable Long cartItemId) {

    }
}
