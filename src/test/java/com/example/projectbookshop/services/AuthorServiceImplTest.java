package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Author;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.AuthorDTO;
import com.example.projectbookshop.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorDTO authorDTO;
    private Book book;
    private Book book2;
    private List<Book> books;

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

        books = List.of(book, book2);

        author = Author.builder().firstName("George")
                .lastName("R.R. Martin")
                .age(60)
                .books(books)
                .build();
        authorDTO = AuthorDTO.builder()
                .firstName("George")
                .lastName("R.R. Martin")
                .age(60)
                .build();
    }

    @Test
    void testGetAllAuthorsReturnsListOfAuthors() {

        when(authorRepository.findAll()).thenReturn(List.of(author));

        List<AuthorDTO> dtos = authorService.getAllAuthors();

        assertThat(dtos).isNotNull();
        assertThat(dtos.size()).isEqualTo(1);
    }

    @Test
    void testGetAuthorByIdReturnAuthorDTO() throws NotFoundException {

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDTO found = authorService.getAuthorById(1L);

        assertThat(found).isNotNull();
    }

    @Test
    void testSaveNewAuthorReturnsDto() {

        when(authorRepository.findByFirstNameAndLastName(any(), any())).thenReturn(null);
        when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);

        AuthorDTO saved = authorService.saveNewAuthor(authorDTO);

        assertThat(saved).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo(author.getFirstName());
    }

    @Test
    void testDeleteAuthorById() {

        when(authorRepository.findById(any())).thenReturn(Optional.of(author));

        assertAll(() -> authorService.deleteAuthorById(author.getId()));
    }

    @Test
    void testModifyAuthor() throws NotFoundException {
        when(authorRepository.findById(any())).thenReturn(Optional.of(author));
        when(authorRepository.save(author)).thenReturn(author);

        AuthorDTO updated = authorService.modifyAuthor(1L, authorDTO);

        assertThat(updated).isNotNull();
    }
}