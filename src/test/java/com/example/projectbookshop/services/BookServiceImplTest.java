package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Author;
import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.AuthorDTO;
import com.example.projectbookshop.model.BookDTO;
import com.example.projectbookshop.repositories.AuthorRepository;
import com.example.projectbookshop.repositories.BasketRepository;
import com.example.projectbookshop.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    AuthorRepository authorRepository;
    @Mock
    BasketRepository basketRepository;

    @InjectMocks
    BookServiceImpl bookService;

    private Author author;
    private AuthorDTO authorDTO;
    private Book book;
    private Book book2;
    private Book book3;
    private BookDTO bookDTO;
    private List<Book> books;
    private Basket basket;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .name("GoT")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();

        book2 = Book.builder()
                .id(2L)
                .name("GoT2")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();

        book3 = Book.builder()
                .name("HP")
                .price(BigDecimal.ONE)
                .numberOfPages(212)
                .build();

        bookDTO = BookDTO.builder()
                .name("GoT2")
                .price(BigDecimal.TEN)
                .authorFullName("George R.R. Martin")
                .numberOfPages(232)
                .build();

        books = List.of(book, book2);

        author = Author.builder().firstName("George")
                .lastName("R.R. Martin")
                .age(60)
                .build();
        authorDTO = AuthorDTO.builder()
                .firstName("George")
                .lastName("R.R. Martin")
                .age(60)
                .build();
        basket = Basket.builder()
                .books(List.of(book3))
                .build();
    }

    @Test
    void testListAllBooksReturnsList() {

        for(Book book : books) {
            book.setAuthor(author);
        }

        when(bookRepository.findAll()).thenReturn(books);

        List<BookDTO> bookDTOS = bookService.listAllBooks();

        assertThat(bookDTOS).isNotNull();
        assertThat(bookDTOS.size()).isEqualTo(2);
    }

    @Test
    void testCreateNewBookToAuthor() throws NotFoundException {
        book.setAuthor(author);
        when(authorRepository.findById(any())).thenReturn(Optional.of(author));
        when(bookRepository.findByName(Mockito.any(String.class))).thenReturn(null);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        BookDTO dto = bookService.createBookToAuthor(1L, bookDTO);

        assertThat(dto).isNotNull();
    }

    @Test
    void testDeleteBookById() {

        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(basketRepository.findAll()).thenReturn(List.of(basket));

        assertAll(() -> bookService.deleteBookById(book.getId()));
    }

    @Test
    void testModifyBookById() throws NotFoundException {
        book.setAuthor(author);
        book2.setAuthor(author);
        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book2);

        BookDTO dto = bookService.modifyBookById(book2.getId(), bookDTO);

        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo(book2.getName());
    }
}