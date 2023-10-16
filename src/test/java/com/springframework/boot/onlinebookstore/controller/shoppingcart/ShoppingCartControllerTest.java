package com.springframework.boot.onlinebookstore.controller.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.boot.onlinebookstore.dto.cartitem.CartItemDto;
import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.springframework.boot.onlinebookstore.model.Role;
import com.springframework.boot.onlinebookstore.model.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
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
                    new ClassPathResource("database/user/delete-user-from-users-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/add-user-to-user-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories"
                            + "/add-firstcategory-to-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-onebook-to-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/shoppingcart"
                            + "/add-shoppingcart-to-shoppingcart-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/cartitem/add-cartitem-to-cartitem-table.sql")
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
                    new ClassPathResource("database/cartitem"
                            + "/delete-cartitems-from-cartitems-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/shoppingcart"
                            + "/delete-shoppingcarts-from-shoppingcart-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/delete-user-from-users-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/delete-books-from-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories"
                            + "/delete-category-from-categories-table.sql")
            );
        }
    }

    @Test
    @DisplayName(value = "Get shopping cart")
    @WithMockUser(username = "batman@gmail.org", roles = "USER")
    void givenUser_whenGetShoppingCart_thenReturnShoppingCart() throws Exception {
        ShoppingCartDto expected = createShoppingCartDto();
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), ShoppingCartDto.class);
        assertNotNull(actual);
        assertEquals(1, actual.getCartItems().size());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @Test
    @DisplayName(value = "Add book to cart")
    @WithMockUser(username = "batman@gmail.org", roles = "USER")
    void givenBook_whenAddBookToCart_thenReturnUpdatedCart() throws Exception {
        CreateShoppingCartRequestDto shoppingCartRequestDto = createShoppingCartRequestDto();
        ShoppingCartDto expected = createShoppingCartDto();
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(shoppingCartRequestDto)))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), ShoppingCartDto.class);
        assertNotNull(actual);
        assertEquals(expected.getCartItems().size(), actual.getCartItems().size());
        assertEquals(expected.getCartItems().get(0), actual.getCartItems().get(0));
    }

    @Test
    @DisplayName(value = "Update book quantity")
    @WithMockUser(username = "batman@gmail.org", roles = "USER")
    void givenNewBookQuantity_whenUpdateQuantity_thenReturnUpdatedShoppingCart() throws Exception {
        Long cartItemId = 1L;
        CreateCartItemRequestDto cartItemRequestDto = createCartItemRequestDto();
        ShoppingCartDto expected = createShoppingCartDto();
        expected.getCartItems().get(0).setQuantity(10);
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put("/cart/cart-items/{cartItemId}",
                                        cartItemId)
                                .content(objectMapper.writeValueAsString(cartItemRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), ShoppingCartDto.class);
        assertNotNull(actual);
        assertEquals(expected.getCartItems().get(0).getQuantity(), actual
                .getCartItems().get(0).getQuantity());
    }

    @Test
    @DisplayName(value = "Delete cart item")
    @WithMockUser(username = "batman@gmail.org", roles = "USER")
    void givenCartItemId_whenDeleteCartItemFromShoppingCart_thenSuccess()
            throws Exception {
        Long cartItemId = 1L;
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/cart/cart-items/{cartItemId}",
                                cartItemId))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private CreateCartItemRequestDto createCartItemRequestDto() {
        CreateCartItemRequestDto createCartItemRequestDto
                = new CreateCartItemRequestDto();
        createCartItemRequestDto.setQuantity(10);
        return createCartItemRequestDto;
    }

    private CreateShoppingCartRequestDto createShoppingCartRequestDto() {
        CreateShoppingCartRequestDto shoppingCartRequestDto
                = new CreateShoppingCartRequestDto();
        shoppingCartRequestDto.setBookId(1L);
        shoppingCartRequestDto.setQuantity(1);
        return shoppingCartRequestDto;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("batman@gmail.org");
        user.setPassword("$2a$12$k19d2/swo89NSkUzxe62ROVcI5lhi1EliUKi208NEsko5h1KVQqLy");
        user.setFirstName("Bruce");
        user.setLastName("Wayne");
        user.setShippingAddress("Gotham city, cave");
        user.setRoles(Set.of(getRole()));
        user.setDeleted(false);
        return user;
    }

    private Role getRole() {
        Role role = new Role();
        role.setId(6L);
        role.setName(Role.RoleName.ROLE_USER);
        return role;
    }

    private ShoppingCartDto createShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        CartItemDto cartItemDto = createCartItemDto();
        shoppingCartDto.setCartItems(List.of(cartItemDto));
        shoppingCartDto.setUserId(1L);
        return shoppingCartDto;
    }

    private CartItemDto createCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setBookTitle("Kobzar");
        cartItemDto.setQuantity(1);
        cartItemDto.setBookId(1L);
        return cartItemDto;
    }
}
