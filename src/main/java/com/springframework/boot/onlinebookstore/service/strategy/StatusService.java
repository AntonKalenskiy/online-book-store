package com.springframework.boot.onlinebookstore.service.strategy;

import com.springframework.boot.onlinebookstore.model.Order;

public interface StatusService {
    Order.Status getStatus();
}
