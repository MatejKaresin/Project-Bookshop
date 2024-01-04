package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.entities.BookshopUser;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookshopUserDTO;
import com.example.projectbookshop.model.BookshopUserDTOForFrontEnd;
import com.example.projectbookshop.model.BookshopUserLoginDTO;
import com.example.projectbookshop.model.BookshopUserSingupDTO;
import com.example.projectbookshop.repositories.BookRepository;
import com.example.projectbookshop.repositories.BookshopUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookshopUserServiceImplTest {

    @Mock
    BookshopUserRepository bookshopUserRepository;
    @Mock
    BookServiceImpl bookService;
    @Mock
    BasketService basketService;
    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookshopUserServiceImpl bookshopUserService;

    private BookshopUser user;
    private BookshopUser userLogged;
    private BookshopUserDTO userDTO;
    private Basket basket;
    private Basket newBasket;
    private Book book;
    private Book book2;
    private Book book3;
    private List<Book> books = new ArrayList<>();
    private List<String> bookNames = new ArrayList<>();
    private BigDecimal totalPrice;
    private BookshopUserSingupDTO signupDTO;
    private BookshopUserLoginDTO loginDTO;
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
                .id(3L)
                .name("GoT3")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();

        books.add(book);
        books.add(book2);

        basket = Basket.builder()
                .id(1L)
                .books(books)
                .build();

        user = BookshopUser.builder()
                .id(1L)
                .nickName("Zlatko")
                .email("zlatko@gmail.com")
                .password("lozinka")
                .logged(false)
                .basket(basket)
                .build();

        userLogged = BookshopUser.builder()
                .id(2L)
                .nickName("Zlatko")
                .email("zlatko@gmail.com")
                .password("lozinka")
                .logged(true)
                .basket(basket)
                .build();

        bookNames.add("GoT");
        bookNames.add("GoT2");

        newBasket = Basket.builder().build();

        userDTO = BookshopUserDTO.builder()
                .id(1L)
                .nickName("Zlatko")
                .build();

        totalPrice = BigDecimal.TEN;

        signupDTO = BookshopUserSingupDTO.builder()
                .nickName("Matko")
                .email("matko@gmail.com")
                .password("lozinka")
                .repeatedPassword("lozinka")
                .build();

        loginDTO = BookshopUserLoginDTO.builder()
                .nickName("Matko")
                .password("lozinka")
                .build();
    }

    @Test
    void testListAllUsers() {
        when(bookshopUserRepository.findAll()).thenReturn(List.of(user));
        when(bookService.getBookNames(any())).thenReturn(bookNames);

        List<BookshopUserDTO> dtos = bookshopUserService.listAllUsers();

        assertThat(dtos).isNotNull();
    }

    @Test
    void testGetUserById() throws NotFoundException {
        when(bookshopUserRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        when(bookService.getBookNames(any())).thenReturn(bookNames);

        BookshopUserDTO dto = bookshopUserService.getBookshopUserById(user.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testCreateNewBookshopUserReturnsUser() {
        when(bookshopUserRepository.findByNickName(Mockito.any(String.class))).thenReturn(null);
        when(basketService.createBasket()).thenReturn(newBasket);
        when(bookshopUserRepository.save(Mockito.any(BookshopUser.class))).thenReturn(user);

        BookshopUserDTO dto = bookshopUserService.createNewBookshopUser(userDTO);

        assertThat(dto).isNotNull();

    }

    @Test
    void testDeleteBookshopUser() throws NotFoundException {
        when(bookshopUserRepository.findById(any())).thenReturn(Optional.of(user));

        assertAll(() -> bookshopUserService.deleteBookshopUserById(user.getId()));
    }

    @Test
    void testAddBookInBasketToUser() throws NotFoundException {
        when(bookshopUserRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookRepository.findById(any())).thenReturn(Optional.of(book3));
        when(basketService.calculateTotalPrice(Mockito.any(Basket.class))).thenReturn(totalPrice);
        when(bookshopUserRepository.save(Mockito.any(BookshopUser.class))).thenReturn(user);
        when(bookService.getBookNames(any())).thenReturn(bookNames);

        BookshopUserDTO dto = bookshopUserService.addBookInBasket(user.getId(), book3.getId());

        assertThat(dto).isNotNull();
        assertThat(dto.getBookNames().size()).isEqualTo(2);
    }

    @Test
    void testRemoveBookFromUsersBasket() throws NotFoundException {
        when(bookshopUserRepository.findById(any())).thenReturn(Optional.of(user));
        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(basketService.calculateTotalPrice(Mockito.any(Basket.class))).thenReturn(totalPrice);
        when(bookshopUserRepository.save(Mockito.any(BookshopUser.class))).thenReturn(user);
        when(bookService.getBookNames(any())).thenReturn(bookNames);

        BookshopUserDTO dto = bookshopUserService.removeBookInBasket(user.getId(), book.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testSignupUser() {
        when(bookshopUserRepository.findAll()).thenReturn(List.of(user));
        when(basketService.createBasket()).thenReturn(newBasket);
        when(bookshopUserRepository.save(Mockito.any(BookshopUser.class))).thenReturn(user);

        ResponseEntity<String> response = bookshopUserService.signupUser(signupDTO);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testLoginUser() {
        when(bookshopUserRepository.findByNickNameAndPassword(any(), any())).thenReturn(user);
        when(bookshopUserRepository.save(Mockito.any(BookshopUser.class))).thenReturn(userLogged);

        BookshopUserDTOForFrontEnd dto = bookshopUserService.loginUser(loginDTO);

        assertThat(dto).isNotNull();
        assertThat(dto.getNickName()).isEqualTo(userLogged.getNickName());
    }

    @Test
    void testUserLogout() {
        when(bookshopUserRepository.findByNickNameAndPassword(any(), any())).thenReturn(userLogged);
        when(bookshopUserRepository.save(Mockito.any(BookshopUser.class))).thenReturn(user);

        BookshopUserDTOForFrontEnd dto = bookshopUserService.logoutUser(loginDTO);

        assertThat(dto).isNotNull();
        assertThat(dto.getLogged()).isFalse();
    }
}