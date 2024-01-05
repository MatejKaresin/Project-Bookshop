package com.example.projectbookshop.controllers;

import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.entities.BookshopUser;
import com.example.projectbookshop.model.BookshopUserDTO;
import com.example.projectbookshop.model.BookshopUserDTOForFrontEnd;
import com.example.projectbookshop.model.BookshopUserLoginDTO;
import com.example.projectbookshop.model.BookshopUserSingupDTO;
import com.example.projectbookshop.services.BookshopUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookshopUserController.class)
class BookshopUserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookshopUserService bookshopUserService;

    private BookshopUser user;
    private BookshopUserDTO userDTOId;
    private BookshopUserDTO userDTO;
    private BookshopUserSingupDTO singupDTO;
    private BookshopUserDTOForFrontEnd dtoForFrontEnd;
    private BookshopUserLoginDTO loginDTO;
    private Book book;
    private ResponseEntity<String> response;

    @BeforeEach
    void setUp() {
        user = BookshopUser.builder()
                .id(1L)
                .nickName("Zlatko")
                .email("zlatko@gmail.com")
                .password("lozinka")
                .logged(false)
                .build();

        userDTOId = BookshopUserDTO.builder()
                .id(1L)
                .nickName("Zlatko")
                .build();

        userDTO = BookshopUserDTO.builder()
                .nickName("Zlatko")
                .build();

        singupDTO = BookshopUserSingupDTO.builder()
                .nickName("Matko")
                .email("matko@gmail.com")
                .password("lozinka")
                .repeatedPassword("lozinka")
                .build();

        dtoForFrontEnd = BookshopUserDTOForFrontEnd.builder()
                .id(1L)
                .nickName("Matko")
                .password("lozinka")
                .logged(true)
                .build();

        loginDTO = BookshopUserLoginDTO.builder()
                .nickName("Matko")
                .password("lozinka")
                .build();

        book = Book.builder()
                .id(1L)
                .name("GoT")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();

        response = new ResponseEntity<>("Successful Signup.", HttpStatus.OK);
    }

    @Test
    void testListAllUsers() throws Exception {

        given(bookshopUserService.listAllUsers()).willReturn(List.of(userDTOId));

        mockMvc.perform(get("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)));

    }

    @Test
    void testGetUserById() throws Exception {

        given(bookshopUserService.getBookshopUserById(any(Long.class))).willReturn(userDTOId);

        mockMvc.perform(get("/api/v1/users/" + userDTOId.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userDTOId.getId().intValue())))
                .andExpect(jsonPath("$.nickName", is(userDTOId.getNickName())));
    }

    @Test
    void testCreateUser() throws Exception {

        given(bookshopUserService.createNewBookshopUser(any(BookshopUserDTO.class))).willReturn(userDTOId);

        mockMvc.perform(post("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserById() throws Exception {

        mockMvc.perform(delete("/api/v1/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookshopUserService).deleteBookshopUserById(longArgumentCaptor.capture());

        assertThat(user.getId()).isEqualTo(longArgumentCaptor.getValue());
    }

    @Test
    void testAddBookToBasket() throws Exception {

        given(bookshopUserService.addBookInBasket(any(Long.class), any(Long.class))).willReturn(userDTOId);

        mockMvc.perform(post("/api/v1/users/" + user.getId() +"/addBook/" + book.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickName", is(user.getNickName())));
    }

    @Test
    void testRemoveBookFromBasket() throws Exception {

        given(bookshopUserService.removeBookInBasket(any(Long.class), any(Long.class))).willReturn(userDTOId);

        mockMvc.perform(post("/api/v1/users/" + user.getId() +"/removeBook/" + book.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickName", is(user.getNickName())));
    }

    @Test
    void testRegistration() throws Exception {

        given(bookshopUserService.signupUser(any(BookshopUserSingupDTO.class))).willReturn(response);

        mockMvc.perform(post("/api/v1/users/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(singupDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginUser() throws Exception {

        given(bookshopUserService.loginUser(any(BookshopUserLoginDTO.class))).willReturn(dtoForFrontEnd);

        mockMvc.perform(post("/api/v1/users/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testLogoutUser() throws Exception {

        given(bookshopUserService.logoutUser(any(BookshopUserLoginDTO.class))).willReturn(dtoForFrontEnd);

        mockMvc.perform(post("/api/v1/users/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }
}