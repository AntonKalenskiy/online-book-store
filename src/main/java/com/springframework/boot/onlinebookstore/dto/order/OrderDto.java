package com.springframework.boot.onlinebookstore.dto.order;

import com.springframework.boot.onlinebookstore.dto.orderitem.OrderItemDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private List<OrderItemDto> orderItems;
}
