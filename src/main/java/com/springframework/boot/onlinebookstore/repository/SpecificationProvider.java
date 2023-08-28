package com.springframework.boot.onlinebookstore.repository;

import com.springframework.boot.onlinebookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();
    Specification<T> getSpecification(String[] params);
}
