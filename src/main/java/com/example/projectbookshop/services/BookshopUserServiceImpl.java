package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.entities.BookshopUser;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookshopUserDTO;
import com.example.projectbookshop.repositories.BookRepository;
import com.example.projectbookshop.repositories.BookshopUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookshopUserServiceImpl implements BookshopUserService {


    private final BookshopUserRepository bookshopUserRepository;
    private final BasketService basketService;
    private final BookService bookService;
    private final BookRepository bookRepository;


    @Autowired
    public BookshopUserServiceImpl(BookshopUserRepository bookshopUserRepository, BasketService basketService, BookService bookService, BookRepository bookRepository) {
        this.bookshopUserRepository = bookshopUserRepository;
        this.basketService = basketService;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookshopUserDTO> listAllUsers() {
        List<BookshopUser> bookshopUsers = bookshopUserRepository.findAll();

        List<BookshopUserDTO> dtos = new ArrayList<>();

        for(BookshopUser bookshopUser : bookshopUsers){
            List<String> bookNames = bookService.getBookNames(bookshopUser.getBasket().getBooks());

            BookshopUserDTO dto = getUserDTO(bookshopUser, bookNames);

            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public BookshopUserDTO getBookshopUserById(Long bookshopUserId) throws NotFoundException {
        BookshopUser bookshopUser = bookshopUserRepository.findById(bookshopUserId).orElseThrow( () -> new NotFoundException("BookshopUser with id: " +
                bookshopUserId + " doesn't exist"));

        List<String> bookNames = bookService.getBookNames(bookshopUser.getBasket().getBooks());

        return getUserDTO(bookshopUser, bookNames);
    }


    @Override
    public BookshopUserDTO createNewBookshopUser(BookshopUserDTO bookshopUserDTO) {
        Basket basket = basketService.createBasket();

        BookshopUser bookshopUser = BookshopUser.builder()
                .name(bookshopUserDTO.getName())
                .basket(basket)
                .build();

        BookshopUser saved = bookshopUserRepository.save(bookshopUser);


        BookshopUserDTO dto = BookshopUserDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();

        return dto;
    }

    @Override
    public void deleteBookshopUserById(Long bookshopUserId) throws NotFoundException {
        BookshopUser bookshopUser = bookshopUserRepository.findById(bookshopUserId).orElseThrow( () -> new NotFoundException("BookshopUser with id: " +
                bookshopUserId + " doesn't exist"));

        bookshopUserRepository.delete(bookshopUser);
    }

    @Override
    public BookshopUserDTO addBookInBasket(Long bookshopUserId, Long bookId) throws NotFoundException {
        BookshopUser bookshopUser = bookshopUserRepository.findById(bookshopUserId).orElseThrow( () -> new NotFoundException("BookshopUser with id: " +
                bookshopUserId + " doesn't exist"));

        Basket basket = bookshopUser.getBasket();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id: " +
                bookId + " doesn't exist"));

        basket.getBooks().add(book);

        BigDecimal totalPrice = basketService.calculateTotalPrice(basket);

        basket.setTotalPrice(totalPrice);

        bookshopUser.setBasket(basket);

        BookshopUser saved = bookshopUserRepository.save(bookshopUser);

        List<String> bookNames = bookService.getBookNames(saved.getBasket().getBooks());

        return getUserDTO(saved, bookNames);

    }

    @Override
    public BookshopUserDTO removeBookInBasket(Long bookshopUserId, Long bookId) throws NotFoundException {
        BookshopUser bookshopUser = bookshopUserRepository.findById(bookshopUserId).orElseThrow( () -> new NotFoundException("BookshopUser with id: " +
                bookshopUserId + " doesn't exist"));

        Basket basket = bookshopUser.getBasket();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id: " +
                bookId + " doesn't exist"));

        basket.getBooks().remove(book);

        BigDecimal totalPrice = basketService.calculateTotalPrice(basket);

        basket.setTotalPrice(totalPrice);

        bookshopUser.setBasket(basket);

        BookshopUser saved = bookshopUserRepository.save(bookshopUser);

        List<String> bookNames = bookService.getBookNames(saved.getBasket().getBooks());

        return getUserDTO(saved, bookNames);

    }

    private static BookshopUserDTO getUserDTO(BookshopUser bookshopUser, List<String> bookNames) {
        BookshopUserDTO dto = BookshopUserDTO.builder()
                .id(bookshopUser.getId())
                .name(bookshopUser.getName())
                .bookNames(bookNames)
                .build();
        return dto;
    }
}
