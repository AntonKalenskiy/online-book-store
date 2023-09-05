package com.springframework.boot.onlinebookstore.repository.category;

import com.springframework.boot.onlinebookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
