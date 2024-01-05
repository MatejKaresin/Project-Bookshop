package com.example.projectbookshop.controllers;

import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookDTO;
import com.example.projectbookshop.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookService bookService;

    private Book book;
    private Book book2;
    private BookDTO bookDTOId;
    private BookDTO bookDTO;
    private List<Book> books;

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

        bookDTOId = BookDTO.builder()
                .id(1L)
                .name("GoT")
                .price(BigDecimal.TEN)
                .authorFullName("George R.R. Martin")
                .numberOfPages(232)
                .build();
        bookDTO = BookDTO.builder()
                .name("GoT2")
                .price(BigDecimal.TEN)
                .authorFullName("George R.R. Martin")
                .numberOfPages(232)
                .build();


    }

    @Test
    void testGetAllBooks() throws Exception {

        given(bookService.listAllBooks()).willReturn(List.of(bookDTOId));

        mockMvc.perform(get("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testCreateNewBook() throws Exception {
        Long authorId = 1L;

        given(bookService.createBookToAuthor(any(), any(BookDTO.class))).willReturn(bookDTOId);

        mockMvc.perform(post("/api/v1/authors/" + authorId + "/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBook() throws Exception {

        mockMvc.perform(delete("/api/v1/books/" + book.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookService).deleteBookById(longArgumentCaptor.capture());

        assertThat(book.getId()).isEqualTo(longArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBook() throws Exception {

        given(bookService.modifyBookById(any(), any(BookDTO.class))).willReturn(bookDTOId);

        mockMvc.perform(put("/api/v1/books/modify/" + bookDTOId.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk());

        verify(bookService).modifyBookById(any(Long.class), any(BookDTO.class));
    }
}