package com.springframework.boot.onlinebookstore.dto.mapper;

import com.springframework.boot.onlinebookstore.config.MapperConfig;
import com.springframework.boot.onlinebookstore.dto.book.BookDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.model.Category;
import com.springframework.boot.onlinebookstore.model.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(CreateShoppingCartRequestDto requestDto);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartDto shoppingCartDto,
                           ShoppingCart shoppingCart) {
        shoppingCartDto.setUserId(shoppingCart.getUser().getId());
    }
}
