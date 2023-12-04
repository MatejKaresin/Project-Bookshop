package com.example.projectbookshop.services;

import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookshopUserDTO;

import java.util.List;

public interface BookshopUserService {
    List<BookshopUserDTO> listAllUsers();

    BookshopUserDTO createNewBookshopUser(BookshopUserDTO bookshopUserDTO);

    void deleteBookshopUserById(Long bookshopUserId) throws NotFoundException;

    BookshopUserDTO getBookshopUserById(Long bookshopUserId) throws NotFoundException;

    BookshopUserDTO addBookInBasket(Long bookshopUserId, Long bookId) throws NotFoundException;

    BookshopUserDTO removeBookInBasket(Long bookshopUserId, Long bookId) throws NotFoundException;
}
