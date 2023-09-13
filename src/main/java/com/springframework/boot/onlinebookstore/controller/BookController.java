package com.springframework.boot.onlinebookstore.controller;

import com.springframework.boot.onlinebookstore.dto.book.BookDto;
import com.springframework.boot.onlinebookstore.dto.book.BookSearchParameters;
import com.springframework.boot.onlinebookstore.dto.book.CreateBookRequestDto;
import com.springframework.boot.onlinebookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all books", description = "Get all available books. "
            + "Includes pagination and sorting")
    public List<BookDto> getBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "Create book", description = "Create a new book")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get book", description = "Get book by id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update book", description = "Update book by id")
    public BookDto updateBookById(@PathVariable Long id,
                                  @RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.updateById(id, bookRequestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete book", description = "Delete book by id")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Search book by parameters", description = "Search book by parameters. "
            + "Includes pagination and sorting")
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        return bookService.search(searchParameters,pageable);
    }
}
