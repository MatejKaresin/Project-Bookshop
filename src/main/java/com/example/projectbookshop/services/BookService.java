package com.example.projectbookshop.services;


import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> listAllBooks();

    BookDTO createBookToAuthor(Long authorId, BookDTO bookDTO) throws NotFoundException;

    void deleteBookById(Long bookId) throws NotFoundException;

    BookDTO modifyBookById(Long bookId, BookDTO bookDTO) throws NotFoundException;

    List<String> getBookNames(List<Book> books);
}
