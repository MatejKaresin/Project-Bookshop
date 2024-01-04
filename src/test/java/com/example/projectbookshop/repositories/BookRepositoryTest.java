package com.example.projectbookshop.repositories;

import com.example.projectbookshop.entities.Author;
import com.example.projectbookshop.entities.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testGetAllBooks() {
        Book book = Book.builder()
                .name("GoT")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();
        Book book2 = Book.builder()
                .name("GoT2")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();
        bookRepository.save(book);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();

        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isEqualTo(2);
    }

    @Test
    void testFindBookByNameReturnsBook() {
        Book book = Book.builder()
                .name("GoT")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .build();
        bookRepository.save(book);

        Book found = bookRepository.findByName(book.getName());

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo(book.getName());
    }

    @Test
    void testFindByAuthorIdReturnsBooks() {
        Author author = Author.builder()
                .firstName("George")
                .lastName("R.R. Martin")
                .age(60)
                .build();

        Author savedAuthor = authorRepository.save(author);

        Book book = Book.builder()
                .name("GoT")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .author(savedAuthor)
                .build();
        Book book2 = Book.builder()
                .name("GoT2")
                .price(BigDecimal.TEN)
                .numberOfPages(232)
                .author(savedAuthor)
                .build();
        bookRepository.save(book);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findByAuthorId(savedAuthor.getId());

        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isEqualTo(2);

    }
}