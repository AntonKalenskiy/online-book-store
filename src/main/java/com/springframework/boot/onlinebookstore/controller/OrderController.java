package com.springframework.boot.onlinebookstore.controller;

import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForAdmin;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForUser;
import com.springframework.boot.onlinebookstore.dto.order.OrderDto;
import com.springframework.boot.onlinebookstore.dto.orderitem.OrderItemDto;
import com.springframework.boot.onlinebookstore.model.User;
import com.springframework.boot.onlinebookstore.service.OrderService;
import com.springframework.boot.onlinebookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;

    @PostMapping
    public OrderDto createOrder(Authentication authentication,
            @RequestBody CreateOrderRequestDtoForUser requestDtoForUser) {
        User user = findUser(authentication);
        return orderService.save(requestDtoForUser, user);
    }

    @GetMapping
    public List<OrderDto> getOrderHistory(Authentication authentication) {
        User user = findUser(authentication);
        return orderService.findAll(user);
    }

    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody CreateOrderRequestDtoForAdmin requestDtoForAdmin) {
        return orderService.updateById(requestDtoForAdmin, id);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(Authentication authentication,
                                            @PathVariable Long orderId) {
        User user = findUser(authentication);
        return orderService.getAll(user, orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getById(Authentication authentication,
                                @PathVariable Long orderId,
                                @PathVariable Long itemId) {
        return null;
    }

    private User findUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.findByEmail(email);
    }
}
