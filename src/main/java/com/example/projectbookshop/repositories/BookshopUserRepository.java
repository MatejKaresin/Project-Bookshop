package com.example.projectbookshop.repositories;

import com.example.projectbookshop.entities.BookshopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookshopUserRepository extends JpaRepository<BookshopUser, Long> {
    BookshopUser findByNickNameAndPassword(String nickname, String password);

    BookshopUser findByNickName(String nickname);
}
