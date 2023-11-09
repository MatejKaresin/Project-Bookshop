package com.example.projectbookshop.controllers;

import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookDTO;
import com.example.projectbookshop.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("books")
    public List<BookDTO> getAllBooks(){
        return bookService.listAllBooks();
    }

    @PostMapping("/authors/{authorId}/books")
    public BookDTO createNewBook(@PathVariable("authorId") Long authorId, @Validated @RequestBody BookDTO bookDTO) throws NotFoundException {
        return bookService.createBookToAuthor(authorId, bookDTO);
    }

    @DeleteMapping("/books/{bookId}")
    public void deleteBook(@PathVariable("bookId") Long bookId) throws NotFoundException {
        bookService.deleteBookById(bookId);
    }

    @PutMapping("/books/modify/{bookId}")
    public BookDTO updateBook(@PathVariable("bookId") Long bookId,
                           @RequestBody BookDTO bookDTO) throws NotFoundException {
        return bookService.modifyBookById(bookId, bookDTO);
    }

}
