package com.springframework.boot.onlinebookstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequestDto {
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String author;
    @NotNull
    @Size(min=8)
    private String isbn;
    @NotNull
    @Min(value = 0)
    private BigDecimal price;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @NotBlank
    private String coverImage;
}
