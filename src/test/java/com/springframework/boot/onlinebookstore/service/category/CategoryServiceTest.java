package com.springframework.boot.onlinebookstore.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import com.springframework.boot.onlinebookstore.dto.category.CategoryDto;
import com.springframework.boot.onlinebookstore.dto.category.CreateCategoryRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.CategoryMapper;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.model.Category;
import com.springframework.boot.onlinebookstore.repository.category.CategoryRepository;
import com.springframework.boot.onlinebookstore.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify saving category to DB")
    public void save_ValidCreateCategoryRequestDto_ReturnValidCategoryDto() {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        Category category = createCategory();
        CategoryDto expected = createCategoryDto();
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        categoryService.save(createCategoryRequestDto());
        verify(categoryMapper).toDto(any());
        verify(categoryMapper).toEntity(any());
        verify(categoryRepository).save(any());
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify if correct list of categoryDto is returned from DB")
    public void findAll_ValidPageable_ReturnAllCategories() {
        Category category1 = createCategory();
        Category category2 = createCategory();
        category2.setId(2L);
        CategoryDto categoryDto1 = createCategoryDto();
        CategoryDto categoryDto2 = createCategoryDto();
        categoryDto2.setId(2L);
        Pageable pageable = PageRequest.of(0, 2);
        List<Category> categories = List.of(category1, category2);
        List<CategoryDto> expected = List.of(categoryDto1, categoryDto2);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);
        List<CategoryDto> actual = categoryService.findAll(pageable);
        assertEquals(actual.size(), expected.size());
        verify(categoryMapper, times(2)).toDto(any());
        verify(categoryRepository).findAll(pageable);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify if correct categoryDto is returned from DB")
    public void findById_WithValidBookId_ReturnValidCategoryDto() {
        Category category = createCategory();
        CategoryDto expected = createCategoryDto();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        categoryService.getById(category.getId());
        verify(categoryRepository).findById(anyLong());
        verify(categoryMapper).toDto(any());
    }

    @Test
    @DisplayName("Verify if there is an exception with invalid id")
    public void findById_WithNotValidCategoryId_ReturnException() {
        Long categoryId = -45L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(categoryId));
        String expected = "Category with id: "
                + categoryId + " not found";
        String actual = exception.getMessage();
        assertEquals(actual, expected);
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Verify if delete method was invoked")
    public void deleteById_WithValidCategoryId_MethodInvoked() {
        Long categoryId = 1L;
        categoryService.deleteById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Display if there is update category by id and categoryRequestDto")
    public void update_WithValidIdAndRequest_ReturnValidCategoryDto() {
        Category category = createCategory();
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        Long categoryId = 1L;
        CategoryDto expected = createCategoryDto();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        categoryService.update(categoryId, requestDto);
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    private CategoryDto createCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Novel");
        categoryDto.setDescription("Interesting and fantastic");
        return categoryDto;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Novel");
        category.setDescription("Interesting and fantastic");
        category.setDeleted(false);
        return category;
    }

    private CreateCategoryRequestDto createCategoryRequestDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Novel");
        requestDto.setDescription("Interesting and fantastic");
        return requestDto;
    }
}
