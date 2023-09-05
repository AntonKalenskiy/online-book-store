package com.springframework.boot.onlinebookstore.service;

import com.springframework.boot.onlinebookstore.dto.BookDto;
import com.springframework.boot.onlinebookstore.dto.BookSearchParameters;
import com.springframework.boot.onlinebookstore.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable);
}
