package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.BookDto;
import com.springframework.boot.onlinebookstore.dto.CreateBookRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.BookMapper;
import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.repository.BookRepository;
import com.springframework.boot.onlinebookstore.service.BookService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto save(CreateBookRequestDto book) {
        Book theBook = bookMapper.toModel(book);
        return bookMapper.toDto(bookRepository.save(theBook));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
