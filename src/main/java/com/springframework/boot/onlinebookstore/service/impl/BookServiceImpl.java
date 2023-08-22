package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.repository.BookRepository;
import com.springframework.boot.onlinebookstore.service.BookService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
