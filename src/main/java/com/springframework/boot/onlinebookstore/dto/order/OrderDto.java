package com.springframework.boot.onlinebookstore.dto.order;

import com.springframework.boot.onlinebookstore.dto.orderitem.OrderItemDto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private List<OrderItemDto> orderItems;
}
