package com.example.projectbookshop.repositories;

import com.example.projectbookshop.entities.BookshopUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BookshopUserRepositoryTest {

    @Autowired
    private BookshopUserRepository bookshopUserRepository;

    @Test
    void testGetAllUsers() {
        BookshopUser bookshopUser = BookshopUser.builder()
                .nickName("Zlatko")
                .email("zlatko@gmail.com")
                .password("lozinka")
                .logged(true)
                .build();
        BookshopUser bookshopUser2 = BookshopUser.builder()
                .nickName("Grga")
                .email("grga@gmail.com")
                .password("lozinka")
                .logged(true)
                .build();
        bookshopUserRepository.save(bookshopUser);
        bookshopUserRepository.save(bookshopUser2);

        List<BookshopUser> users = bookshopUserRepository.findAll();

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void testFindUserByNicknameReturnUser() {
        BookshopUser bookshopUser = BookshopUser.builder()
                .nickName("Zlatko")
                .email("zlatko@gmail.com")
                .password("lozinka")
                .logged(true)
                .build();

        BookshopUser saved = bookshopUserRepository.save(bookshopUser);

        BookshopUser found = bookshopUserRepository.findByNickName(bookshopUser.getNickName());

        assertThat(found).isNotNull();
        assertThat(found.getNickName()).isEqualTo(bookshopUser.getNickName());
    }

    @Test
    void testFindByNicknameAndPasswordReturnsUser() {
        BookshopUser bookshopUser = BookshopUser.builder()
                .nickName("Zlatko")
                .email("zlatko@gmail.com")
                .password("lozinka")
                .logged(true)
                .build();
        BookshopUser saved = bookshopUserRepository.save(bookshopUser);

        BookshopUser found = bookshopUserRepository.findByNickNameAndPassword(bookshopUser.getNickName(), bookshopUser.getPassword());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
    }
}