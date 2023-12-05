package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.entities.BookshopUser;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BookshopUserDTO;
import com.example.projectbookshop.model.BookshopUserFEDTO;
import com.example.projectbookshop.model.BookshopUserLoginDTO;
import com.example.projectbookshop.model.BookshopUserSingupDTO;
import com.example.projectbookshop.repositories.BookRepository;
import com.example.projectbookshop.repositories.BookshopUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        BookshopUser existing = bookshopUserRepository.findByNickName(bookshopUserDTO.getNickName());

        if(existing != null) {
            throw new IllegalStateException("User by that name already exists");
        }

        Basket basket = basketService.createBasket();

        BookshopUser bookshopUser = BookshopUser.builder()
                .nickName(bookshopUserDTO.getNickName())
                .basket(basket)
                .build();

        BookshopUser saved = bookshopUserRepository.save(bookshopUser);


        BookshopUserDTO dto = BookshopUserDTO.builder()
                .id(saved.getId())
                .nickName(saved.getNickName())
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


    @Override
    public ResponseEntity<String> signupUser(BookshopUserSingupDTO singupDTO) {
        List<BookshopUser> bookshopUsers = bookshopUserRepository.findAll();

        checkEmail(singupDTO.getEmail());
        checkPassword(singupDTO.getPassword(), singupDTO.getRepeatedPassword());

        for (BookshopUser bookshopUser : bookshopUsers) {
            if(singupDTO.getNickName().equals(bookshopUser.getNickName())) {
                throw new IllegalStateException("That nickname is taken");
            } if(singupDTO.getEmail().equals(bookshopUser.getEmail())) {
                throw new IllegalStateException("That email is taken");
            }
        }

        Basket basket = basketService.createBasket();

        BookshopUser bookshopUser = BookshopUser.builder()
                .nickName(singupDTO.getNickName())
                .email(singupDTO.getEmail())
                .password(singupDTO.getPassword())
                .logged(false)
                .basket(basket)
                .build();

        bookshopUserRepository.save(bookshopUser);

        String message = "Successful Signup.";

        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    @Override
    public BookshopUserFEDTO loginUser(BookshopUserLoginDTO loginDTO) {
        BookshopUser bookshopUser = bookshopUserRepository.findByNickNameAndPassword(loginDTO.getNickName(), loginDTO.getPassword());

        if(bookshopUser == null) {
            throw new IllegalStateException("User not found");
        }

        if(!bookshopUser.getLogged()) {
            bookshopUser.setLogged(true);
            bookshopUserRepository.save(bookshopUser);
        } else {
            throw new IllegalStateException("User already logged");
        }


        return BookshopUserFEDTO.builder()
                .id(bookshopUser.getId())
                .nickName(bookshopUser.getNickName())
                .logged(bookshopUser.getLogged())
                .build();
    }

    @Override
    public ResponseEntity<String> logoutUser(BookshopUserLoginDTO loginDTO) {
        BookshopUser bookshopUser = bookshopUserRepository.findByNickNameAndPassword(loginDTO.getNickName(), loginDTO.getPassword());

        if(bookshopUser == null) {
            throw new IllegalStateException("You aren't logged in");
        }

        if(bookshopUser.getLogged()) {
            bookshopUser.setLogged(false);
            bookshopUserRepository.save(bookshopUser);
        } else {
            return new  ResponseEntity<>("You aren't logged in", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Logout successful", HttpStatus.OK);

    }

    private void checkEmail(String email) {
        if(!email.contains("@gmail.com")){
            throw new IllegalStateException("Email must contain @gmail.com");
        }
    }

    private void checkPassword(String password, String repeatedPassword) {
        if(!password.equals(repeatedPassword)) {
            throw new IllegalStateException("Passwords don't match");
        }
    }

    private static BookshopUserDTO getUserDTO(BookshopUser bookshopUser, List<String> bookNames) {
        BookshopUserDTO dto = BookshopUserDTO.builder()
                .id(bookshopUser.getId())
                .nickName(bookshopUser.getNickName())
                .bookNames(bookNames)
                .build();
        return dto;
    }
}
