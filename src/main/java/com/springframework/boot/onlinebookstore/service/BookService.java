package com.springframework.boot.onlinebookstore.service;

import com.springframework.boot.onlinebookstore.model.Book;
import java.util.List;

public interface BookService{
    Book save(Book book);
    List<Book> findAll();

}
