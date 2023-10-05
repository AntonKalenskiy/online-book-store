package com.springframework.boot.onlinebookstore.dto.mapper;

import com.springframework.boot.onlinebookstore.config.MapperConfig;
import com.springframework.boot.onlinebookstore.dto.book.BookDto;
import com.springframework.boot.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.springframework.boot.onlinebookstore.dto.book.CreateBookRequestDto;
import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.model.Category;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        List<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId).toList();
        bookDto.setCategoryIds(categoryIds);
    }
}
