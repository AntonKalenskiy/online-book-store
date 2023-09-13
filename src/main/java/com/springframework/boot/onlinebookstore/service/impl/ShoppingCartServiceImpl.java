package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.ShoppingCartMapper;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.model.CartItem;
import com.springframework.boot.onlinebookstore.model.ShoppingCart;
import com.springframework.boot.onlinebookstore.model.User;
import com.springframework.boot.onlinebookstore.repository.cartitem.CartItemRepository;
import com.springframework.boot.onlinebookstore.repository.book.BookRepository;
import com.springframework.boot.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.springframework.boot.onlinebookstore.service.ShoppingCartService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto findCart(User user) {
        ShoppingCart shoppingCart = findShoppingCartByUser(user);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addBookToCart(CreateShoppingCartRequestDto shoppingCartRequestDto,
                                         User user) {
        ShoppingCart shoppingCart = findShoppingCartByUser(user);
        CartItem savedCartItemInDb = createNewCartItem(shoppingCartRequestDto, shoppingCart);
        Set<CartItem> newSetOfItems = shoppingCart.getCartItems();
        newSetOfItems.add(savedCartItemInDb);
        shoppingCart.setCartItems(newSetOfItems);
        ShoppingCart savedShoppingCartInDB = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(savedShoppingCartInDB);
    }

    @Override
    public ShoppingCartDto updateBookQuantity(Long cartItemId,
                                              CreateCartItemRequestDto requestDto,
                                              User user) {
        ShoppingCart shoppingCart = findShoppingCartByUser(user);
        Optional<CartItem> cartItemOptional = findCartItemById(shoppingCart, cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(requestDto.getQuantity());
            cartItemRepository.save(cartItem);
            return shoppingCartMapper.toDto(shoppingCart);
        } else {
            throw new EntityNotFoundException(
                    "There is no such cart-item with id: " + cartItemId + " in the shopping cart");
        }
    }

    @Override
    public void deleteCartItemById(Long cartItemId, User user) {
        ShoppingCart shoppingCart = findShoppingCartByUser(user);
        Optional<CartItem> cartItemOptional = findCartItemById(shoppingCart, cartItemId);
        if (cartItemOptional.isPresent()) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new EntityNotFoundException(
                    "There is no such cart-item with id: " + cartItemId + " in the shopping cart");
        }
    }

    private ShoppingCart findShoppingCartByUser(User user) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser(user);
        if (shoppingCartOptional.isEmpty()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCart.setCartItems(new HashSet<>());
            return shoppingCartRepository.save(shoppingCart);
        }
        return shoppingCartOptional.get();
    }

    private CartItem createNewCartItem(CreateShoppingCartRequestDto shoppingCartRequestDto,
                                       ShoppingCart shoppingCart) {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(shoppingCartRequestDto.getQuantity());
        cartItem.setBook(bookRepository.findById(shoppingCartRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Book with this id wasn't found")
        ));
        cartItem.setShoppingCart(shoppingCart);
        return cartItemRepository.save(cartItem);
    }

    private Optional<CartItem> findCartItemById(ShoppingCart shoppingCart,
                                                Long cartItemId) {
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        return cartItems.stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst();
    }
}
