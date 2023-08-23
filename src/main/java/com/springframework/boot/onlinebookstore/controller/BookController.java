package com.springframework.boot.onlinebookstore.controller;

import com.springframework.boot.onlinebookstore.dto.BookDto;
import com.springframework.boot.onlinebookstore.dto.CreateBookRequestDto;
import com.springframework.boot.onlinebookstore.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @PostMapping()
    public BookDto createBook(CreateBookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }
}
