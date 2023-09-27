package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.book.BookDto;
import com.springframework.boot.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.springframework.boot.onlinebookstore.dto.book.BookSearchParameters;
import com.springframework.boot.onlinebookstore.dto.book.CreateBookRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.BookMapper;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.model.Category;
import com.springframework.boot.onlinebookstore.repository.book.BookRepository;
import com.springframework.boot.onlinebookstore.repository.book.BookSpecificationBuilder;
import com.springframework.boot.onlinebookstore.repository.category.CategoryRepository;
import com.springframework.boot.onlinebookstore.service.BookService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto book) {
        Book theBook = bookMapper.toModel(book);
        theBook.setTitle("fdgfgdf");
        List<Category> categories = categoryRepository.findAllById(book.getCategoryIds());
        theBook.setCategories(new HashSet<>(categories));
        Book savedBook = bookRepository.save(theBook);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
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
            List<Category> categories = categoryRepository
                    .findAllById(bookRequestDto.getCategoryIds());
            existingBook.setCategories(new HashSet<>(categories));
            Book savedBookInDB = bookRepository.save(existingBook);
            return bookMapper.toDto(savedBookInDB);
        } else {
            throw new EntityNotFoundException("Book with id: " + id + " not found");
        }
    }

    @Override
    public List<BookDto> search(BookSearchParameters params, Pageable pageable) {
        Specification<Book> bookSpecification = specificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
