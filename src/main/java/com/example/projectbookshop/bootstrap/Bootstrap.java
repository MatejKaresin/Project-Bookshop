package com.example.projectbookshop.bootstrap;

import com.example.projectbookshop.entities.Author;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.repositories.AuthorRepository;
import com.example.projectbookshop.repositories.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class Bootstrap implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadAuthorAndBook();
    }

    void loadAuthorAndBook(){
        Author author1 = Author.builder()
                .firstName("J.K.")
                .lastName("Rowling")
                .age(45)
                .build();

        Author author2 = Author.builder()
                .firstName("George R.R.")
                .lastName("Martin")
                .age(70)
                .build();

        Author author1Saved = authorRepository.save(author1);
        Author author2Saved = authorRepository.save(author2);

        Book book1 = Book.builder()
                .name("Harry Potter")
                .numberOfPages(230)
                .price(BigDecimal.valueOf(10.59))
                .author(author1Saved)
                .build();

        Book book2 = Book.builder()
                .name("Harry Potter 2")
                .numberOfPages(550)
                .price(BigDecimal.valueOf(14.59))
                .author(author1Saved)
                .build();

        Book book3 = Book.builder()
                .name("Game of Thrones")
                .numberOfPages(800)
                .price(BigDecimal.valueOf(9.59))
                .author(author2Saved)
                .build();

        Book book4 = Book.builder()
                .name("Game of Thrones 2")
                .numberOfPages(970)
                .price(BigDecimal.valueOf(15.59))
                .author(author2Saved)
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
    }
}
