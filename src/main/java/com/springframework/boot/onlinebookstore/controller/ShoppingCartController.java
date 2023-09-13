package com.springframework.boot.onlinebookstore.controller;

import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.model.User;
import com.springframework.boot.onlinebookstore.repository.user.UserRepository;
import com.springframework.boot.onlinebookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping cart management", description
        = "endpoints for managing shopping carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get own shopping cart", description
            = "Get all cart items from own shopping cart "
            + "Includes pagination and sorting")
    public ShoppingCartDto getCart(Authentication authentication) {
        User user = findUser(authentication);
        return shoppingCartService.findCart(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add book to cart", description
            = "Create new cart item and add it to shopping cart")
    public ShoppingCartDto addBookToCart(
            @RequestBody @Valid CreateShoppingCartRequestDto shoppingCartRequestDto,
            Authentication authentication) {
        User user = findUser(authentication);
        return shoppingCartService.addBookToCart(shoppingCartRequestDto, user);
    }

    @PutMapping("cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update books quantity", description
            = "Update books quantity in cart item by id")
    public ShoppingCartDto updateQuantityOfBooks(@PathVariable Long cartItemId,
                                          @RequestBody @Valid CreateCartItemRequestDto requestDto,
                                                 Authentication authentication) {
        User user = findUser(authentication);
        return shoppingCartService.updateBookQuantity(cartItemId, requestDto, user);
    }

    @DeleteMapping("cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete cart item", description
            = "Delete cart item from shopping cart by id")
    public void deleteBook(@PathVariable Long cartItemId,
                           Authentication authentication) {
        User user = findUser(authentication);
        shoppingCartService.deleteCartItemById(cartItemId, user);
    }

    private User findUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with such email doesn't exist")
        );
    }
}
