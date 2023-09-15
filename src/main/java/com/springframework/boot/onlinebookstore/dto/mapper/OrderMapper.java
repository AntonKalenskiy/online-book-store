package com.springframework.boot.onlinebookstore.dto.mapper;

import com.springframework.boot.onlinebookstore.config.MapperConfig;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForAdmin;
import com.springframework.boot.onlinebookstore.dto.order.CreateOrderRequestDtoForUser;
import com.springframework.boot.onlinebookstore.dto.order.OrderDto;
import com.springframework.boot.onlinebookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toModel(CreateOrderRequestDtoForUser requestDtoForUser);
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);
    Order toModel(CreateOrderRequestDtoForAdmin requestDtoForAdmin);
}
