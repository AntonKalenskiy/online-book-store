package com.springframework.boot.onlinebookstore.dto.mapper;

import com.springframework.boot.onlinebookstore.config.MapperConfig;
import com.springframework.boot.onlinebookstore.dto.book.BookDto;
import com.springframework.boot.onlinebookstore.dto.cartitem.CartItemDto;
import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.model.CartItem;
import com.springframework.boot.onlinebookstore.model.Category;
import com.springframework.boot.onlinebookstore.repository.book.BookRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);

    CartItem toModel(CreateCartItemRequestDto cartItemRequestDto);

    @AfterMapping
    default void setBook(@MappingTarget CartItemDto cartItemDto, CartItem cartItem) {
        cartItemDto.setBookTitle(cartItem.getBook().getTitle());
        cartItemDto.setBookId(cartItem.getBook().getId());
    }

}
