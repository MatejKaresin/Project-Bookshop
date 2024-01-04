package com.example.projectbookshop.repositories;

import com.example.projectbookshop.entities.Author;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;


    @Test
    void testGetAllAuthors() {
        Author author = Author.builder()
                .firstName("Ivana")
                .lastName("Brlic")
                .age(21)
                .build();

        Author author2 = Author.builder()
                .firstName("Ivano")
                .lastName("Brlic")
                .age(22)
                .build();
        authorRepository.save(author);
        authorRepository.save(author2);

        List<Author> authors = authorRepository.findAll();

        Assertions.assertThat(authors).isNotNull();
        Assertions.assertThat(authors.size()).isEqualTo(2);
    }

    @Test
    void testFindAuthorById() {
        Author author = Author.builder()
                .firstName("Ivana")
                .lastName("Brlic")
                .age(21)
                .build();

        authorRepository.save(author);

        Author saved = authorRepository.findById(author.getId()).get();

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getFirstName()).isEqualTo(author.getFirstName());
    }

    @Test
    void testDeleteAuthorIsEmpty() {
        Author author = Author.builder()
                .firstName("Ivana")
                .lastName("Brlic")
                .age(21)
                .build();
        
        Author saved = authorRepository.save(author);

        authorRepository.delete(author);

        Optional<Author> deleted = authorRepository.findById(saved.getId());

        Assertions.assertThat(deleted).isEmpty();
    }

    @Test
    void testFindByFirstAndLastNameWorks() {
        Author author = Author.builder()
                .firstName("Ivana")
                .lastName("Brlic")
                .age(21)
                .build();
        Author saved = authorRepository.save(author);

        Author found = authorRepository.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());

        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getId()).isEqualTo(saved.getId());
    }
}