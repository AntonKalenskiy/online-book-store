package com.springframework.boot.onlinebookstore.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCategoryRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 2)
    private String name;
    @NotNull
    @NotBlank
    @Size(min = 2)
    private String description;
}
