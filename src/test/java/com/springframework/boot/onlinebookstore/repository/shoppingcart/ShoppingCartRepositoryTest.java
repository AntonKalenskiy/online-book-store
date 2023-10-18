package com.springframework.boot.onlinebookstore.repository.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springframework.boot.onlinebookstore.model.Role;
import com.springframework.boot.onlinebookstore.model.ShoppingCart;
import com.springframework.boot.onlinebookstore.model.User;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartRepositoryTest(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Test
    @DisplayName("Find shopping cart where user. Expected correct shopping cart")
    @Sql(
            scripts = {
                    "classpath:database/user/delete-user-from-users-table.sql",
                    "classpath:database/user/add-user-to-user-table.sql",
                    "classpath:database/categories/add-firstcategory-to-categories-table.sql",
                    "classpath:database/books/add-onebook-to-books-table.sql",
                    "classpath:database/shoppingcart/add-shoppingcart-to-shoppingcart-table.sql",
                    "classpath:database/cartitem/add-cartitem-to-cartitem-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    "classpath:database/cartitem/delete-cartitems-from-cartitems-table.sql",
                    "classpath:database/shoppingcart"
                            + "/delete-shoppingcarts-from-shoppingcart-table.sql",
                    "classpath:database/user/delete-user-from-users-table.sql",
                    "classpath:database/books/delete-books-from-books-table.sql",
                    "classpath:database/categories/delete-category-from-categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findShoppingCartByUser_ValidUser_ReturnShoppingCart() {
        User user = createUser();
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser(user);
        assertTrue(optionalShoppingCart.isPresent());
        assertEquals(optionalShoppingCart.get().getUser(), user);
        assertFalse(optionalShoppingCart.get().getCartItems().isEmpty());
    }

    @Test
    @DisplayName("Find shopping cart where not exist user. Expected optional is empty")
    @Sql(
            scripts = {
                    "classpath:database/user/delete-user-from-users-table.sql",
                    "classpath:database/user/add-user-to-user-table.sql",
                    "classpath:database/categories/add-firstcategory-to-categories-table.sql",
                    "classpath:database/books/add-onebook-to-books-table.sql",
                    "classpath:database/shoppingcart/add-shoppingcart-to-shoppingcart-table.sql",
                    "classpath:database/cartitem/add-cartitem-to-cartitem-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    "classpath:database/cartitem/delete-cartitems-from-cartitems-table.sql",
                    "classpath:database/shoppingcart"
                            + "/delete-shoppingcarts-from-shoppingcart-table.sql",
                    "classpath:database/user/delete-user-from-users-table.sql",
                    "classpath:database/books/delete-books-from-books-table.sql",
                    "classpath:database/categories/delete-category-from-categories-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findShoppingCartByUser_NotValidUser_ReturnEmptyOptional() {
        User user = createUser();
        user.setId(2L);
        user.setEmail("joker@gmail.org");
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser(user);
        assertTrue(optionalShoppingCart.isEmpty());
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
}
