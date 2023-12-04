package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.entities.BookshopUser;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BasketDTO;
import com.example.projectbookshop.repositories.BasketRepository;
import com.example.projectbookshop.repositories.BookshopUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final BookshopUserRepository bookshopUserRepository;
    private final BookService bookService;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository, BookshopUserRepository bookshopUserRepository, BookService bookService) {
        this.basketRepository = basketRepository;
        this.bookshopUserRepository = bookshopUserRepository;
        this.bookService = bookService;
    }


    @Override
    public List<BasketDTO> listAll() {
        List<Basket> baskets = basketRepository.findAll();

        List<BasketDTO> dtos = new ArrayList<>();


        for(Basket basket : baskets){

            BigDecimal totalPrice = BigDecimal.ZERO;
            List<String> bookNames = new ArrayList<>();
            for (Book book : basket.getBooks()){
                bookNames.add(book.getName());
                totalPrice = totalPrice.add(book.getPrice());
            }

            BasketDTO dto = BasketDTO.builder()
                    .id(basket.getId())
                    .bookNames(bookNames)
                    .totalPrice(totalPrice)
                    .build();
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public Basket createBasket() {
        Basket basket = basketRepository.save(new Basket());

        return basket;
    }

    @Override
    public BigDecimal calculateTotalPrice(Basket basket) {

        BigDecimal totalPrice = BigDecimal.ZERO;
        for(Book book : basket.getBooks()){
            totalPrice = totalPrice.add(book.getPrice());
        }

        return totalPrice;
    }

    @Override
    public BasketDTO getBasketForUser(Long userId) throws NotFoundException {
        BookshopUser bookshopUser = bookshopUserRepository.findById(userId).orElseThrow(() -> new NotFoundException("BookshopUser with id: " +
                userId + " doesn't exist"));

        List<String> bookNames = bookService.getBookNames(bookshopUser.getBasket().getBooks());

        BasketDTO basketDTO = BasketDTO.builder()
                .id(bookshopUser.getBasket().getId())
                .bookNames(bookNames)
                .totalPrice(bookshopUser.getBasket().getTotalPrice())
                .build();

        return basketDTO;
    }
}
