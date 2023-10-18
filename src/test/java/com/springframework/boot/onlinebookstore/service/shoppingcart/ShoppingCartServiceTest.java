package com.springframework.boot.onlinebookstore.service.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.springframework.boot.onlinebookstore.dto.cartitem.CartItemDto;
import com.springframework.boot.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.ShoppingCartMapper;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.CreateShoppingCartRequestDto;
import com.springframework.boot.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.model.CartItem;
import com.springframework.boot.onlinebookstore.model.Category;
import com.springframework.boot.onlinebookstore.model.Role;
import com.springframework.boot.onlinebookstore.model.ShoppingCart;
import com.springframework.boot.onlinebookstore.model.User;
import com.springframework.boot.onlinebookstore.repository.book.BookRepository;
import com.springframework.boot.onlinebookstore.repository.cartitem.CartItemRepository;
import com.springframework.boot.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.springframework.boot.onlinebookstore.service.impl.ShoppingCartServiceImpl;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartServiceImpl;

    @Test
    @DisplayName("Verify if find cart by user")
    public void givenUser_whenFindCart_thenReturnShoppingCart() {
        User user = createUser();
        CartItem cartItem = createCartItem();
        ShoppingCart shoppingCart = createShoppingCart(user, cartItem);
        ShoppingCartDto shoppingCartDto = createShoppingCartDto();
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        shoppingCartServiceImpl.findCart(user);
        verify(shoppingCartRepository).findByUser(user);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Verify adding book to shopping cart with exist cart item")
    public void givenShoppingCartWithBook_whenAddBookToCart_thenReturnNewShoppingCart() {
        User user = createUser();
        CartItem cartItem = createCartItem();
        ShoppingCart shoppingCart = createShoppingCart(user, cartItem);
        ShoppingCartDto shoppingCartDto = createShoppingCartDto();
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        CreateShoppingCartRequestDto shoppingCartRequestDto
                = createShoppingCartRequestDto();
        shoppingCartServiceImpl.addBookToCart(shoppingCartRequestDto, user);
        verify(shoppingCartRepository).findByUser(user);
        verify(cartItemRepository).save(cartItem);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository,
                cartItemRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Verify adding book to empty shopping cart")
    public void givenEmptyShoppingCart_whenAddBookToCart_thenReturnShoppingCartWithBook() {
        User user = createUser();
        ShoppingCart shoppingCart = createEmptyShoppingCart(user);
        Book book = createBook();
        CartItem cartItem = createCartItem();
        when(shoppingCartRepository.findByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(book));
        when(cartItemRepository.save(any(CartItem.class)))
                .thenReturn(cartItem);
        when(shoppingCartRepository.save(shoppingCart))
                .thenReturn(shoppingCart);
        CreateShoppingCartRequestDto shoppingCartRequestDto
                = createShoppingCartRequestDto();
        ShoppingCartDto shoppingCartDto = createShoppingCartDto();
        when(shoppingCartMapper.toDto(shoppingCart))
                .thenReturn(shoppingCartDto);
        shoppingCartServiceImpl.addBookToCart(shoppingCartRequestDto, user);
        verify(shoppingCartRepository).findByUser(user);
        verify(bookRepository).findById(anyLong());
        verify(cartItemRepository).save(any(CartItem.class));
        verify(shoppingCartRepository).save(shoppingCart);
        verify(shoppingCartMapper).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Update quantity of books in shopping cart")
    public void givenShoppingCartWithActualCartItem_whenUpdateCart_thenReturnUpdatedShoppingCart() {
        User user = createUser();
        CartItem cartItem = createCartItem();
        cartItem.setQuantity(10);
        ShoppingCartDto shoppingCartDto = createShoppingCartDto();
        shoppingCartDto.getCartItems().get(0).setQuantity(10);
        ShoppingCart shoppingCart = createShoppingCart(user, cartItem);
        when(shoppingCartRepository.findByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(shoppingCart))
                .thenReturn(shoppingCartDto);
        CreateCartItemRequestDto cartItemRequestDto
                = createCartItemRequestDto();
        Long cartItemId = 1L;
        shoppingCartServiceImpl.updateBookQuantity(cartItemId,
                cartItemRequestDto, user);
        verify(shoppingCartRepository).findByUser(user);
        verify(cartItemRepository).save(cartItem);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository,
                cartItemRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Update not existing cart item in shopping cart")
    public void givenNotExistCartItemId_whenUpdateBookQuantity_thenEntityNotFoundException() {
        CreateCartItemRequestDto cartItemRequestDto
                = createCartItemRequestDto();
        Long cartItemId = -45L;
        User user = createUser();
        CartItem cartItem = createCartItem();
        ShoppingCart shoppingCart = createShoppingCart(user, cartItem);
        when(shoppingCartRepository.findByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartServiceImpl.updateBookQuantity(cartItemId,
                        cartItemRequestDto, user));
        String expected = "There is no such cart-item with id: "
                + cartItemId + " in the shopping cart";
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(shoppingCartRepository).findByUser(user);
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName("Delete cart item by id")
    public void givenCartItemId_whenDeleteCartItem_thenSuccess() {
        Long cartItemId = 1L;
        User user = createUser();
        CartItem cartItem = createCartItem();
        ShoppingCart shoppingCart = createShoppingCart(user, cartItem);
        when(shoppingCartRepository.findByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        shoppingCartServiceImpl.deleteCartItemById(cartItemId, user);
        verify(shoppingCartRepository).findByUser(user);
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName("Delete no exist cart item by id")
    public void givenNotExistCartItemId_whenDeleteCartItem_thenEntityNotFoundException() {
        Long cartItemId = 999L;
        User user = createUser();
        CartItem cartItem = createCartItem();
        ShoppingCart shoppingCart = createShoppingCart(user, cartItem);
        when(shoppingCartRepository.findByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartServiceImpl.deleteCartItemById(cartItemId, user));
        String expected = "There is no such cart-item with id: "
                + cartItemId + " in the shopping cart";
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(shoppingCartRepository).findByUser(user);
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    private CreateCartItemRequestDto createCartItemRequestDto() {
        CreateCartItemRequestDto cartItemRequestDto
                = new CreateCartItemRequestDto();
        cartItemRequestDto.setQuantity(10);
        return cartItemRequestDto;
    }

    private ShoppingCart createEmptyShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setDeleted(false);
        return shoppingCart;
    }

    private ShoppingCartDto createShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        CartItemDto cartItemDto = createCartItemDto();
        shoppingCartDto.setCartItems(List.of(cartItemDto));
        return shoppingCartDto;
    }

    private CartItemDto createCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setBookTitle("Kobzar");
        cartItemDto.setQuantity(3);
        cartItemDto.setBookId(1L);
        return cartItemDto;
    }

    private CreateShoppingCartRequestDto createShoppingCartRequestDto() {
        CreateShoppingCartRequestDto shoppingCartRequestDto
                = new CreateShoppingCartRequestDto();
        shoppingCartRequestDto.setBookId(1L);
        shoppingCartRequestDto.setQuantity(3);
        return shoppingCartRequestDto;
    }

    private CartItem createCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(createBook());
        cartItem.setQuantity(3);
        cartItem.setDeleted(false);
        return cartItem;
    }

    private Book createBook() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Taras Shevchenko");
        book.setIsbn("12345678");
        book.setPrice(new BigDecimal("45.9"));
        book.setTitle("Kobzar");
        Category category = createCategory();
        book.setCategories(Set.of(category));
        book.setCoverImage("11223344");
        book.setDescription("Interesting");
        book.setDeleted(false);
        return book;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Novel");
        category.setDescription("Interesting book");
        category.setDeleted(false);
        return category;
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

    private ShoppingCart createShoppingCart(User user, CartItem cartItem) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(Set.of(cartItem));
        shoppingCart.setId(1L);
        shoppingCart.setDeleted(false);
        return shoppingCart;
    }
}
