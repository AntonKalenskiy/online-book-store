package com.springframework.boot.onlinebookstore.dto.mapper;

import com.springframework.boot.onlinebookstore.config.MapperConfig;
import com.springframework.boot.onlinebookstore.dto.user.CreateUserRequestDto;
import com.springframework.boot.onlinebookstore.dto.user.UserDto;
import com.springframework.boot.onlinebookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toModel(CreateUserRequestDto requestDto);
}
