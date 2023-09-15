package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.mapper.OrderMapper;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForAdmin;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForUser;
import com.springframework.boot.onlinebookstore.dto.order.OrderDto;
import com.springframework.boot.onlinebookstore.dto.orderitem.OrderItemDto;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.exception.ShoppingCartNotExistOrEmptyException;
import com.springframework.boot.onlinebookstore.model.*;
import com.springframework.boot.onlinebookstore.repository.order.OrderRepository;
import com.springframework.boot.onlinebookstore.repository.orderitem.OrderItemRepository;
import com.springframework.boot.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.springframework.boot.onlinebookstore.service.OrderService;
import com.springframework.boot.onlinebookstore.service.strategy.StatusStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final StatusStrategy statusStrategy;

    @Override
    public OrderDto save(CreateOrderRequestDtoForUser orderRequestDtoForUser, User user) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser(user);
        if (optionalShoppingCart.isPresent() && !optionalShoppingCart.get().getCartItems().isEmpty()) {
            Order savedOrderInDb = createOrder(orderRequestDtoForUser, user);
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            Set<OrderItem> orderItems = createOrderItems(savedOrderInDb, shoppingCart.getCartItems());
            BigDecimal totalPrice = calculateTotalPrice(orderItems);
            savedOrderInDb.setOrderItems(orderItems);
            savedOrderInDb.setTotal(totalPrice);
            orderRepository.save(savedOrderInDb);
            shoppingCart.setCartItems(new HashSet<>());
            shoppingCartRepository.save(shoppingCart);
            return orderMapper.toDto(savedOrderInDb);
        } else {
            throw new ShoppingCartNotExistOrEmptyException("User doesn't have a shopping cart or shopping cart is empty");
        }
    }

    @Override
    public List<OrderDto> findAll(User user) {
        return orderRepository.findAllByUser(user).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto updateById(CreateOrderRequestDtoForAdmin orderRequestDtoForAdmin,
                               Long id) {
        String status = orderRequestDtoForAdmin.getStatus();
        Order.Status theStatus = statusStrategy.getStatusService(status).getStatus();
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(theStatus);
            orderRepository.save(order);
            return orderMapper.toDto(order);
        } else {
            throw new EntityNotFoundException("Order with such id wasn't found: " + id);
        }
    }

    @Override
    public List<OrderItemDto> getAll(User user, Long orderId) {
        return null;
    }

    @Override
    public OrderItemDto getById(User user, Long orderId, Long itemId) {
        return null;
    }


    private Order createOrder(CreateOrderRequestDtoForUser orderRequestDtoForUser, User user) {
        Order order = new Order();
        order.setShippingAddress(orderRequestDtoForUser.getShippingAddress());
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        return orderRepository.save(order);
    }

    private Set<OrderItem> createOrderItems(Order order, Set<CartItem> cartItems) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = createOrderItem(cartItem, order);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private OrderItem createOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);
        return orderItem;
    }

    private BigDecimal calculateTotalPrice(Set<OrderItem> orderItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            totalPrice = totalPrice.add(orderItem.getPrice());
        }
        return totalPrice;
    }
}
