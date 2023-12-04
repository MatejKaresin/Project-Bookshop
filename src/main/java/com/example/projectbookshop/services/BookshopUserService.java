package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.BookshopUser;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookshopUserDTO;
import com.example.projectbookshop.model.BookshopUserLoginDTO;
import com.example.projectbookshop.model.BookshopUserSingupDTO;

import java.util.List;

public interface BookshopUserService {
    List<BookshopUserDTO> listAllUsers();

    BookshopUserDTO createNewBookshopUser(BookshopUserDTO bookshopUserDTO);

    void deleteBookshopUserById(Long bookshopUserId) throws NotFoundException;

    BookshopUserDTO getBookshopUserById(Long bookshopUserId) throws NotFoundException;

    BookshopUserDTO addBookInBasket(Long bookshopUserId, Long bookId) throws NotFoundException;

    BookshopUserDTO removeBookInBasket(Long bookshopUserId, Long bookId) throws NotFoundException;

    BookshopUser signupUser(BookshopUserSingupDTO singupDTO);

    BookshopUserDTO loginUser(BookshopUserLoginDTO loginDTO);
}
