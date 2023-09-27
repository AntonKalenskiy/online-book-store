package com.springframework.boot.onlinebookstore.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.model.Category;
import com.springframework.boot.onlinebookstore.repository.book.BookRepository;
import com.springframework.boot.onlinebookstore.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository,
                              CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Test
    @DisplayName("Find all books where category id. Expected two books")
    @Sql(
            scripts = {
                    "classpath:database/categories/add-firstcategory-to-categories-table.sql",
                    "classpath:database/books/add-twobooks-to-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    "classpath:database/books/delete-twobooks-from-books-table.sql",
                    "classpath:database/categories/delete-category-from-category-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findAllByCategoryId_ValidCategoryId_ReturnListOfTwoBook() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 2);
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId, pageable);
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("Find all books where category id. Expected one book")
    @Sql(
            scripts = {
                    "classpath:database/categories/add-firstcategory-to-categories-table.sql",
                    "classpath:database/books/add-onebook-to-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {"classpath:database/books/delete-onebook-from-book-table.sql",
                    "classpath:database/categories/delete-category-from-category-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findAllByCategoryId_ValidCategoryId_ReturnListOfOneBook() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 2);
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId, pageable);
        assertEquals(1, actual.size());
    }
}
