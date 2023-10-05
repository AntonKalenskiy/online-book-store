package com.springframework.boot.onlinebookstore.controller.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.boot.onlinebookstore.dto.book.BookDto;
import com.springframework.boot.onlinebookstore.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class BookControllerTest {
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
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-twobooks-to-books-table.sql")
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
                    new ClassPathResource("database/books/delete-books-from-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/delete-category-from-categories-table.sql")
            );
        }
    }

    @Test
    @DisplayName(value = "Create book")
    @WithMockUser(roles = "ADMIN")
    void givenBook_whenSaveValidBookToDb_thenSuccess() throws Exception {
        CreateBookRequestDto bookRequestDto = createBookRequestDto();
        bookRequestDto.setIsbn("98765432");
        BookDto expected = createBookDto();
        expected.setIsbn("98765432");
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(bookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @DisplayName("Get all books")
    @WithMockUser(roles = "USER")
    void givenListOfBooks_whenGetAll_thenReturnAllBooks() throws Exception {
        String params = "?page=0&size=3";
        BookDto bookDto1 = createBookDto();
        BookDto bookDto2 = createBookDto();
        bookDto2.setTitle("Zapovit");
        bookDto2.setIsbn("12345679");
        bookDto2.setCoverImage("11223355");
        List<BookDto> expected = List.of(bookDto1, bookDto2);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books" + params))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected.get(0).getIsbn(), actual[0].getIsbn());
    }

    @Test
    @DisplayName("Get book by id")
    @WithMockUser(roles = "USER")
    void givenBookDto_whenGetBookById_thenReturnBook() throws Exception {
        BookDto expected = createBookDto();
        Long bookId = expected.getId();
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/books/{bookId}",
                                bookId))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertEquals(actual.getTitle(), expected.getTitle());
        assertEquals(actual.getIsbn(), expected.getIsbn());
    }

    @Test
    @DisplayName("Get book by not exist id")
    @WithMockUser(roles = "USER")
    void givenBookDtoId_whenGetBookById_thenReturnEntityNotFoundException() throws Exception {
        Long notExistBookId = 777L;
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{bookId}",
                        notExistBookId))
                .andExpect(status().isNotFound())
                .andReturn();
        Exception exception = mvcResult.getResolvedException();
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Book with id: " + notExistBookId + " not found");
    }

    @Test
    @DisplayName("Update book")
    @WithMockUser(roles = "ADMIN")
    void givenCreateBookRequest_whenUpdateBookById_thenReturnChangedBookDto() throws Exception {
        Long bookId = 1L;
        CreateBookRequestDto bookRequestDto = createBookRequestDto();
        bookRequestDto.setTitle("Lastivka");
        BookDto expected = createBookDto();
        expected.setTitle("Lastivka");
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/books/{id}",
                                bookId)
                                .content(objectMapper.writeValueAsString(bookRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertEquals(actual.getTitle(), expected.getTitle());
        assertEquals(actual.getIsbn(), expected.getIsbn());
    }

    @Test
    @DisplayName("Delete book")
    @WithMockUser(roles = "ADMIN")
    void givenBookId_whenDeleteBookById_thenReturnOk() throws Exception {
        Long bookId = 1L;
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/books/{id}",
                                        bookId))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Search books by parameters")
    @WithMockUser(roles = "USER")
    void givenSearchParameters_whenSearch_thenListOfBooks() throws Exception {
        String params = "?authors=Taras Shevchenko&page=0";
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/search" + params))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] bookDtos = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), BookDto[].class);
        assertNotNull(bookDtos);
        assertEquals(2, bookDtos.length);
    }

    private BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setAuthor("Taras Shevchenko");
        bookDto.setIsbn("12345678");
        bookDto.setPrice(new BigDecimal("45.90"));
        bookDto.setTitle("Kobzar");
        bookDto.setCategoryIds(List.of(1L));
        bookDto.setCoverImage("11223344");
        bookDto.setDescription("Interesting");
        return bookDto;
    }

    private CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("Taras Shevchenko");
        bookRequestDto.setIsbn("12345678");
        bookRequestDto.setPrice(new BigDecimal("45.90"));
        bookRequestDto.setTitle("Kobzar");
        bookRequestDto.setCategoryIds(List.of(1L));
        bookRequestDto.setCoverImage("11223344");
        bookRequestDto.setDescription("Interesting");
        return bookRequestDto;
    }
}
