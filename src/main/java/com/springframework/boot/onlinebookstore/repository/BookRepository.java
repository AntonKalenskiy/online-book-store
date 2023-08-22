package com.springframework.boot.onlinebookstore.repository;

import com.springframework.boot.onlinebookstore.model.Book;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
