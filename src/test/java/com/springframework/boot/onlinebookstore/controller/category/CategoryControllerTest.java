package com.springframework.boot.onlinebookstore.controller.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.boot.onlinebookstore.dto.category.CategoryDto;
import com.springframework.boot.onlinebookstore.dto.category.CreateCategoryRequestDto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/add-firstcategory-to-categories-table.sql")
            );
        }
    }

    @AfterEach
    void afterEach(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/delete-category-from-categories-table.sql")
            );
        }
    }

    @Test
    @DisplayName(value = "Create category")
    @WithMockUser(roles = "ADMIN")
    void givenCreateCategoryDto_whenSaveValidCategory_thenSuccess() throws Exception {
        CreateCategoryRequestDto createCategoryRequestDto = createCategoryRequestDto();
        createCategoryRequestDto.setName("Poem");
        CategoryDto expected = createCategoryDto();
        expected.setName("Poem");
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(createCategoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, expected);
    }

    @Test
    @DisplayName("Get all categories")
    @WithMockUser(roles = "USER")
    void givenListOfCategories_whenGetAll_thenReturnAllCategories() throws Exception {
        String params = "?page=0&size=3";
        CategoryDto categoryDto = createCategoryDto();
        List<CategoryDto> expected = List.of(categoryDto);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/categories" + params))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected.get(0).getName(), actual[0].getName());
    }

    @Test
    @DisplayName("Get category by id")
    @WithMockUser(roles = "USER")
    void givenCreateCategoryDto_whenGetCategoryById_thenReturnCategory() throws Exception {
        CategoryDto expected = createCategoryDto();
        Long categoryId = expected.getId();
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/{categoryId}",
                                categoryId))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getDescription(), expected.getDescription());
    }

    @Test
    @DisplayName("Get category by not exist id")
    @WithMockUser(roles = "USER")
    void givenCategoryDtoId_whenGetCategoryById_thenReturnEntityNotFoundException() throws Exception {
        Long notExistCategoryId = 777L;
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/{categoryId}",
                                notExistCategoryId))
                .andExpect(status().isNotFound())
                .andReturn();
        Exception exception = mvcResult.getResolvedException();
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Category with id: "
                + notExistCategoryId + " not found");
    }

    @Test
    @DisplayName("Update category")
    @WithMockUser(roles = "ADMIN")
    void givenCreateCategoryRequest_whenUpdateCategoryById_thenReturnChangedCategoryDto() throws Exception {
        Long categoryId = 1L;
        CreateCategoryRequestDto categoryRequestDto = createCategoryRequestDto();
        categoryRequestDto.setName("Fantasy");
        CategoryDto expected = createCategoryDto();
        expected.setName("Fantasy");
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/categories/{id}",
                                        categoryId)
                                .content(objectMapper.writeValueAsString(categoryRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getDescription(), expected.getDescription());
    }

    @Test
    @DisplayName("Delete category")
    @WithMockUser(roles = "ADMIN")
    void givenCategoryId_whenDeleteCategoryById_thenReturnOk() throws Exception {
        Long categoryId = 1L;
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/categories/{id}",
                                categoryId))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private CategoryDto createCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Novel");
        categoryDto.setDescription("Interesting and fantastic");
        return categoryDto;
    }

    private CreateCategoryRequestDto createCategoryRequestDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Novel");
        requestDto.setDescription("Interesting and fantastic");
        return requestDto;
    }
}
