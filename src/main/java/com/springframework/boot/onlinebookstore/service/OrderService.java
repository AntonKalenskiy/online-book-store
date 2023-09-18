package com.springframework.boot.onlinebookstore.service;

import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForAdmin;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForUser;
import com.springframework.boot.onlinebookstore.dto.order.OrderDto;
import com.springframework.boot.onlinebookstore.dto.orderitem.OrderItemDto;
import com.springframework.boot.onlinebookstore.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto save(CreateOrderRequestDtoForUser orderRequestDtoForUser,
                                 User user);

    List<OrderDto> findAll(User user, Pageable pageable);

    OrderDto updateById(CreateOrderRequestDtoForAdmin orderRequestDtoForAdmin,
                        Long id);

    List<OrderItemDto> getAll(User user, Long orderId, Pageable pageable);

    OrderItemDto getById(User user, Long orderId, Long itemId);
}
