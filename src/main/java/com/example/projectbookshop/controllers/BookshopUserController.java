package com.example.projectbookshop.controllers;

import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookshopUserDTO;
import com.example.projectbookshop.services.BookshopUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookshopUserController {

    private final BookshopUserService bookshopUserService;


    @Autowired
    public BookshopUserController(BookshopUserService bookshopUserService) {
        this.bookshopUserService = bookshopUserService;
    }

    @GetMapping("/users")
    public List<BookshopUserDTO> getUsers(){
        return bookshopUserService.listAllUsers();
    }

    @GetMapping("/users/{userId}")
    public BookshopUserDTO getUserId(@PathVariable("userId") Long userId) throws NotFoundException {
        return bookshopUserService.getBookshopUserById(userId);

    }

    @PostMapping("/users")
    public BookshopUserDTO createUser(@Validated @RequestBody BookshopUserDTO bookshopUserDTO){
        return bookshopUserService.createNewBookshopUser(bookshopUserDTO);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) throws NotFoundException {
        bookshopUserService.deleteBookshopUserById(userId);
    }

    @PostMapping("/users/{userId}/addBook/{bookId}")
    public BookshopUserDTO addBookToBasket(@PathVariable("userId") Long userId,
                                           @PathVariable("bookId") Long bookId) throws NotFoundException {
        return bookshopUserService.addBookInBasket(userId, bookId);
    }

    @PostMapping("/users/{userId}/removeBook/{bookId}")
    public BookshopUserDTO removeBookFromBasket(@PathVariable("userId") Long userId,
                                                @PathVariable("bookId") Long bookId) throws NotFoundException {
        return bookshopUserService.removeBookInBasket(userId, bookId);
    }


}
