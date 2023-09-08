package com.springframework.boot.onlinebookstore.controller;

import com.springframework.boot.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.springframework.boot.onlinebookstore.dto.category.CategoryDto;
import com.springframework.boot.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.springframework.boot.onlinebookstore.service.BookService;
import com.springframework.boot.onlinebookstore.service.CategoryService;
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

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category management", description = "endpoints for managing categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create category", description = "Create a new category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all categories", description = "Get all available categories. "
            + "Includes pagination and sorting")
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category", description = "Get category by id")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update category", description = "Update category by id")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category", description = "Delete category by id")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get books by category", description = "Get books by category id")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id, Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
