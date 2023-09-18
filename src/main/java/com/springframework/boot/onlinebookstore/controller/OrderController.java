package com.springframework.boot.onlinebookstore.controller;

import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForAdmin;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForUser;
import com.springframework.boot.onlinebookstore.dto.order.OrderDto;
import com.springframework.boot.onlinebookstore.dto.orderitem.OrderItemDto;
import com.springframework.boot.onlinebookstore.model.User;
import com.springframework.boot.onlinebookstore.service.OrderService;
import com.springframework.boot.onlinebookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order management", description = "endpoints for managing shopping carts")
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create order", description
            = "Create new order")
    public OrderDto createOrder(Authentication authentication,
            @RequestBody CreateOrderRequestDtoForUser requestDtoForUser) {
        User user = findUser(authentication);
        return orderService.save(requestDtoForUser, user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get own order history", description
            = "Get all orders from data base. "
            + "Includes pagination and sorting")
    public List<OrderDto> getOrderHistory(Authentication authentication, Pageable pageable) {
        User user = findUser(authentication);
        return orderService.findAll(user, pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order status", description
            = "Update order status by id")
    public OrderDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody CreateOrderRequestDtoForAdmin requestDtoForAdmin) {
        return orderService.updateById(requestDtoForAdmin, id);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get list of order items from order", description
            = "Get list of orders from order by id. "
            + "Includes pagination and sorting")
    public List<OrderItemDto> getOrderItems(Authentication authentication,
                                            @PathVariable Long orderId,
                                            Pageable pageable) {
        User user = findUser(authentication);
        return orderService.getAll(user, orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get order item by id from order by id", description
            = "Get order item by id from order by id.")
    public OrderItemDto getById(Authentication authentication,
                                @PathVariable Long orderId,
                                @PathVariable Long itemId) {
        User user = findUser(authentication);
        return orderService.getById(user, orderId, itemId);
    }

    private User findUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.findByEmail(email);
    }
}
