package com.springframework.boot.onlinebookstore.service.strategy.impl;

import com.springframework.boot.onlinebookstore.model.Order;
import com.springframework.boot.onlinebookstore.service.strategy.StatusService;
import org.springframework.stereotype.Component;

@Component
public class PendingStatusService implements StatusService {
    @Override
    public Order.Status getStatus() {
        return Order.Status.PENDING;
    }
}
