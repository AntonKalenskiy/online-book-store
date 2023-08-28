package com.springframework.boot.onlinebookstore.service;

import com.springframework.boot.onlinebookstore.dto.BookDto;
import com.springframework.boot.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
