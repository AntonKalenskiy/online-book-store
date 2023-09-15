package com.springframework.boot.onlinebookstore.service.strategy;

import com.springframework.boot.onlinebookstore.exception.StatusNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatusStrategy {
    private final List<StatusService> statusServices;

    public StatusService getStatusService(String status) {
        return statusServices.stream()
                .filter(st -> st.getStatus().name().equals(status.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new StatusNotExistException("This status doesn't exist: " + status));
    }
}