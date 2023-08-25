package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.BookDto;
import com.springframework.boot.onlinebookstore.dto.CreateBookRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.BookMapper;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.repository.BookRepository;
import com.springframework.boot.onlinebookstore.service.BookService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

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

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id: " + id + " not found"))
        );
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto bookRequestDto) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            existingBook.setTitle(bookRequestDto.getTitle());
            existingBook.setAuthor(bookRequestDto.getAuthor());
            existingBook.setIsbn(bookRequestDto.getIsbn());
            existingBook.setPrice(bookRequestDto.getPrice());
            existingBook.setDescription(bookRequestDto.getDescription());
            existingBook.setCoverImage(bookRequestDto.getCoverImage());
            Book savedBookInDB = bookRepository.save(existingBook);
            return bookMapper.toDto(savedBookInDB);
        } else {
            throw new EntityNotFoundException("Book with id: " + id + " not found");
        }
    }
}
