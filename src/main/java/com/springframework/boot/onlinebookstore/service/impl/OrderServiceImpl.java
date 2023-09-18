package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.mapper.OrderItemMapper;
import com.springframework.boot.onlinebookstore.dto.mapper.OrderMapper;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForAdmin;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForUser;
import com.springframework.boot.onlinebookstore.dto.order.OrderDto;
import com.springframework.boot.onlinebookstore.dto.orderitem.OrderItemDto;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.exception.ShoppingCartNotExistOrEmptyException;
import com.springframework.boot.onlinebookstore.model.CartItem;
import com.springframework.boot.onlinebookstore.model.Order;
import com.springframework.boot.onlinebookstore.model.OrderItem;
import com.springframework.boot.onlinebookstore.model.ShoppingCart;
import com.springframework.boot.onlinebookstore.model.User;
import com.springframework.boot.onlinebookstore.repository.order.OrderRepository;
import com.springframework.boot.onlinebookstore.repository.orderitem.OrderItemRepository;
import com.springframework.boot.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.springframework.boot.onlinebookstore.service.OrderService;
import com.springframework.boot.onlinebookstore.service.strategy.StatusStrategy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final StatusStrategy statusStrategy;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto save(CreateOrderRequestDtoForUser orderRequestDtoForUser, User user) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser(user);
        if (optionalShoppingCart.isPresent()
                && !optionalShoppingCart.get().getCartItems().isEmpty()) {
            Order savedOrderInDb =
                    createOrder(orderRequestDtoForUser, user);
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            Set<OrderItem> orderItems =
                    createOrderItems(savedOrderInDb, shoppingCart.getCartItems());
            BigDecimal totalPrice = calculateTotalPrice(orderItems);
            savedOrderInDb.setOrderItems(orderItems);
            savedOrderInDb.setTotal(totalPrice);
            orderRepository.save(savedOrderInDb);
            shoppingCart.setCartItems(new HashSet<>());
            shoppingCartRepository.save(shoppingCart);
            return orderMapper.toDto(savedOrderInDb);
        } else {
            throw new ShoppingCartNotExistOrEmptyException("User doesn't have a shopping cart "
                    + "or shopping cart is empty");
        }
    }

    @Override
    public List<OrderDto> findAll(User user, Pageable pageable) {
        return orderRepository.findAllByUser(user, pageable).stream()
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
            throw new EntityNotFoundException("Order with such id: " + id + " wasn't found");
        }
    }

    @Override
    public List<OrderItemDto> getAll(User user, Long orderId, Pageable pageable) {
        getOrderByIdOfUser(user, orderId);
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId, pageable);
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getById(User user, Long orderId, Long itemId) {
        Order orderById = getOrderByIdOfUser(user, orderId);
        getOrderItemByIdInOrderByIdOfUser(orderById, itemId);
        return orderItemMapper.toDto(orderItemRepository.findById(itemId).orElseThrow(
                () -> new EntityNotFoundException("OrderItem with such id: " + itemId
                        + " wasn't found in the order")
        ));
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
        orderItem.setPrice(cartItem.getBook().getPrice()
                .multiply(new BigDecimal(orderItem.getQuantity())));
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

    private Order getOrderByIdOfUser(User user,
                                     Long orderId) {
        List<Order> orderList = orderRepository.findAllByUser(user);
        Optional<Order> optionalOrder = orderList.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new EntityNotFoundException("Order with such id: "
                    + orderId + " wasn't found");
        }
    }

    private void getOrderItemByIdInOrderByIdOfUser(Order orderById, Long itemId) {
        Optional<OrderItem> optionalOrderItem = orderById.getOrderItems().stream()
                .filter(orderItem -> orderItem.getId().equals(itemId))
                .findFirst();
        if (optionalOrderItem.isEmpty()) {
            throw new EntityNotFoundException("OrderItem with such id: "
                    + itemId + " wasn't found in the order");
        }
    }
}
