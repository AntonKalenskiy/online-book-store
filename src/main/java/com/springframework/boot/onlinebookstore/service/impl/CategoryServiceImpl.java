package com.springframework.boot.onlinebookstore.service.impl;

import com.springframework.boot.onlinebookstore.dto.category.CategoryDto;
import com.springframework.boot.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.CategoryMapper;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.model.Category;
import com.springframework.boot.onlinebookstore.repository.category.CategoryRepository;
import com.springframework.boot.onlinebookstore.service.CategoryService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: "
                        + id + " not found")));
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toEntity(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(requestDto.getName());
            category.setDescription(requestDto.getDescription());
            return categoryMapper.toDto(categoryRepository.save(category));
        } else {
            throw new EntityNotFoundException("Category with id: "
                    + id + " not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
