package com.example.projectbookshop.controllers;

import com.example.projectbookshop.entities.Author;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.AuthorDTO;
import com.example.projectbookshop.services.AuthorService;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;
    private AuthorDTO authorDTOId;
    private Book book;
    private Book book2;
    private List<Book> books = new ArrayList<>();
    @BeforeEach
    void setUp() {
        book = Book.builder()
                .name("GoT")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();

        book2 = Book.builder()
                .name("GoT2")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();

        books.add(book);
        books.add(book2);

        author = Author.builder()
                .id(1L)
                .firstName("George")
                .lastName("R.R. Martin")
                .age(60)
                .books(books)
                .build();

        authorDTO = AuthorDTO.builder()
                .firstName("George")
                .lastName("R.R. Martin")
                .age(60)
                .build();
        authorDTOId = AuthorDTO.builder()
                .id(1L)
                .firstName("George")
                .lastName("R.R. Martin")
                .age(60)
                .build();
    }

    @Test
    void testListBeers() throws Exception {

        given(authorService.getAllAuthors()).willReturn(List.of(authorDTO));

        mockMvc.perform(get("/api/v1/authors")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testGetAuthorById() throws Exception {

        given(authorService.getAuthorById(author.getId())).willReturn(authorDTOId);

        mockMvc.perform(get("/api/v1/authors/" + author.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(authorDTOId.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(authorDTOId.getFirstName())));

    }

    @Test
    void testCreateAuthor() throws Exception {

        given(authorService.saveNewAuthor(any(AuthorDTO.class))).willReturn(authorDTOId);

        mockMvc.perform(post("/api/v1/authors")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAuthor() throws Exception {

        mockMvc.perform(delete("/api/v1/authors/" + author.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(authorService).deleteAuthorById(longArgumentCaptor.capture());

        assertThat(author.getId()).isEqualTo(longArgumentCaptor.getValue());
    }

    @Test
    void testUpdateAuthor() throws Exception {

        given(authorService.modifyAuthor(any(), any(AuthorDTO.class))).willReturn(authorDTOId);

        mockMvc.perform(put("/api/v1/authors/modify/" + authorDTOId.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorDTO)))
                .andExpect(status().isOk());

        verify(authorService).modifyAuthor(any(Long.class), any(AuthorDTO.class));
    }
}