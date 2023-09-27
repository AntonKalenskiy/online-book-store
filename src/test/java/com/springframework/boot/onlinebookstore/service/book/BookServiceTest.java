package com.springframework.boot.onlinebookstore.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import com.springframework.boot.onlinebookstore.dto.book.BookDto;
import com.springframework.boot.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import com.springframework.boot.onlinebookstore.dto.book.BookSearchParameters;
import com.springframework.boot.onlinebookstore.dto.book.CreateBookRequestDto;
import com.springframework.boot.onlinebookstore.dto.mapper.BookMapper;
import com.springframework.boot.onlinebookstore.exception.EntityNotFoundException;
import com.springframework.boot.onlinebookstore.model.Book;
import com.springframework.boot.onlinebookstore.model.Category;
import com.springframework.boot.onlinebookstore.repository.book.BookRepository;
import com.springframework.boot.onlinebookstore.repository.book.BookSpecificationBuilder;
import com.springframework.boot.onlinebookstore.repository.category.CategoryRepository;
import com.springframework.boot.onlinebookstore.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookSpecificationBuilder specificationBuilder;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    @DisplayName("Verify saving book to DB")
    public void save_ValidCreateBookRequestDto_ShouldReturnValidBookDto() {
        //given
        CreateBookRequestDto bookRequestDto = crateBookRequestDto();
        Book book = createBook();
        Category category = createCategory();
        BookDto expected = createBookDto();

        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(categoryRepository.findAllById(List.of(1L))).thenReturn(List.of(category));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        //when
        BookDto actual = bookServiceImpl.save(bookRequestDto);
        //then
        assertEquals(actual.getAuthor(), expected.getAuthor());
        verify(bookRepository).save(book);
        verify(bookMapper).toModel(bookRequestDto);
        verify(categoryRepository).findById(anyLong());
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, categoryRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify if correct list of bookDto is returned from DB")
    public void findAll_ValidPageable_ReturnsAllProducts() {
        //given
        Book book1 = createBook();
        Book book2 = createBook();
        book2.setId(2L);
        BookDto bookDto1 = createBookDto();
        BookDto bookDto2 = createBookDto();
        bookDto2.setId(2L);
        Pageable pageable = PageRequest.of(0, 5);
        List<Book> books = List.of(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book1)).thenReturn(bookDto1);
        when(bookMapper.toDto(book2)).thenReturn(bookDto2);
        List<BookDto> expected = List.of(bookDto1, bookDto2);
        //when
        List<BookDto> bookDtos = bookServiceImpl.findAll(pageable);
        int actual = bookDtos.size();
        //Then
        assertEquals(actual, expected.size());
        verify(bookMapper, times(2)).toDto(any());
        verify(bookRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }


    @Test
    @DisplayName("Verify if correct bookDto is returned from DB")
    public void findById_WithValidBookId_ShouldReturnValidBookDto() {
        //given
        Book book = createBook();
        BookDto expected = createBookDto();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        //when
        BookDto actual = bookServiceImpl.findById(book.getId());
        //then
        assertEquals(actual.getAuthor(), expected.getAuthor());
        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getCategoryIds(), expected.getCategoryIds());
        assertEquals(actual.getPrice(), expected.getPrice());
        assertEquals(actual.getIsbn(), expected.getIsbn());
    }

    @Test
    @DisplayName("Verify if there is an exception with invalid id")
    public void findById_WithNotValidBookId_ShouldReturnException() {
        //given
        Long bookId = -45L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        //when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.findById(bookId));
        String expected = "Book with id: " + bookId + " not found";
        String actual = exception.getMessage();
        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify if delete method was invoked")
    public void deleteById_WithValidBookId_MethodInvoked() {
        Long bookId = 1L;
        bookServiceImpl.deleteById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify if there is update book by id and bookRequestDto")
    public void updateById_WithValidIdAndRequest_ReturnValidBookDto() {
        Book book = createBook();
        CreateBookRequestDto bookRequestDto = crateBookRequestDto();
        Long bookId = 1L;
        BookDto bookDto = createBookDto();
        Category category = createCategory();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(categoryRepository.findAllById(any())).thenReturn(List.of(category));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        bookServiceImpl.updateById(bookId, bookRequestDto);

        assertEquals(bookRequestDto.getAuthor(), bookDto.getAuthor());
        assertEquals(bookRequestDto.getTitle(), bookDto.getTitle());
        assertEquals(bookRequestDto.getPrice(), bookDto.getPrice());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(book);
        verify(categoryRepository, times(1)).findAllById(any());
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Verify if search method works correctly")
    public void search_WithValidParams_ReturnBookDtoByParams() {
        String[] authorNames = new String[]{"Taras Shevchenko"};
        String[] titles = new String[]{"Kobzar"};
        BookSearchParameters bookSearchParameters = new BookSearchParameters(authorNames, titles);
        Pageable pageable = PageRequest.of(0, 2);

        Book book = createBook();
        BookDto bookDto = createBookDto();

        List<Book> books = List.of(book);
        List<BookDto> bookDtos = List.of(bookDto);
        Specification<Book> specification = mock(Specification.class);

        when(specificationBuilder.build(bookSearchParameters)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(new PageImpl<>(books));
        when(bookMapper.toDto(any())).thenReturn(bookDtos.get(0));

        List<BookDto> actual = bookServiceImpl.search(bookSearchParameters, pageable);

        assertEquals(1, actual.size());
        verify(bookRepository, times(1)).findAll(specification, pageable);
        for (Book theBook : books) {
            verify(bookMapper, times(1)).toDto(theBook);
        }
    }

    @Test
    @DisplayName("Verify if there are books by category id")
    public void findAllByCategoryId_ValidCategoryId_ReturnListOfBookWithSuchCategory() {
        Long categoryId = 1L;
        Book book1 = createBook();
        Book book2 = createBook();
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds1 = createBookDtoWithoutCategoryIds();
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds2 = createBookDtoWithoutCategoryIds();

        List<Book> booksWithOneCategory = List.of(book1, book2);
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds1,
                bookDtoWithoutCategoryIds2);
        Pageable pageable = PageRequest.of(0, 2);

        when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(booksWithOneCategory);
        when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(bookDtoWithoutCategoryIds1);
        when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(bookDtoWithoutCategoryIds2);

        List<BookDtoWithoutCategoryIds> actual = bookServiceImpl.findAllByCategoryId(categoryId, pageable);

        assertEquals(actual.size(), expected.size());
        verify(bookRepository, times(1)).findAllByCategoryId(any(), any());
        verify(bookMapper, times(2)).toDtoWithoutCategories(any());
    }

    private CreateBookRequestDto crateBookRequestDto() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("Taras Shevchenko");
        bookRequestDto.setIsbn("12345678");
        bookRequestDto.setPrice(new BigDecimal("45.9"));
        bookRequestDto.setTitle("Kobzar");
        bookRequestDto.setCategoryIds(List.of(1L));
        bookRequestDto.setCoverImage("11223344");
        bookRequestDto.setDescription("Interesting");
        return bookRequestDto;
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

    private BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setAuthor("Taras Shevchenko");
        bookDto.setIsbn("12345678");
        bookDto.setPrice(new BigDecimal("45.9"));
        bookDto.setTitle("Kobzar");
        bookDto.setCategoryIds(List.of(1L));
        bookDto.setCoverImage("11223344");
        bookDto.setDescription("Interesting");
        return bookDto;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Novel");
        category.setDescription("Interesting book");
        category.setDeleted(false);
        return category;
    }

    private BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds1 = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds1.setId(1L);
        bookDtoWithoutCategoryIds1.setAuthor("Taras Shevchenko");
        bookDtoWithoutCategoryIds1.setIsbn("12345678");
        bookDtoWithoutCategoryIds1.setPrice(new BigDecimal("45.9"));
        bookDtoWithoutCategoryIds1.setTitle("Kobzar");
        bookDtoWithoutCategoryIds1.setCoverImage("11223344");
        bookDtoWithoutCategoryIds1.setDescription("Interesting");
        return bookDtoWithoutCategoryIds1;
    }
}
