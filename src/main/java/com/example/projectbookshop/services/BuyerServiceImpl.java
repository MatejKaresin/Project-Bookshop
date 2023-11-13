package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.entities.Buyer;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BuyerDTO;
import com.example.projectbookshop.repositories.BookRepository;
import com.example.projectbookshop.repositories.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuyerServiceImpl implements BuyerService {


    private final BuyerRepository buyerRepository;
    private final BasketService basketService;
    private final BookService bookService;
    private final BookRepository bookRepository;


    @Autowired
    public BuyerServiceImpl(BuyerRepository buyerRepository, BasketService basketService, BookService bookService, BookRepository bookRepository) {
        this.buyerRepository = buyerRepository;
        this.basketService = basketService;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BuyerDTO> listAllBuyers() {
        List<Buyer> buyers = buyerRepository.findAll();

        List<BuyerDTO> dtos = new ArrayList<>();

        for(Buyer buyer : buyers){
            List<String> bookNames = bookService.getBookNames(buyer.getBasket().getBooks());

            BuyerDTO dto = getBuyerDTO(buyer, bookNames);

            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public BuyerDTO getBuyerById(Long buyerId) throws NotFoundException {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow( () -> new NotFoundException("Buyer with id: " +
                buyerId + " doesn't exist"));

        List<String> bookNames = bookService.getBookNames(buyer.getBasket().getBooks());

        return getBuyerDTO(buyer, bookNames);
    }


    @Override
    public BuyerDTO createNewBuyer(BuyerDTO buyerDTO) {
        Basket basket = basketService.createBasket();

        Buyer buyer = Buyer.builder()
                .name(buyerDTO.getName())
                .basket(basket)
                .build();

        Buyer saved = buyerRepository.save(buyer);


        BuyerDTO dto = BuyerDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();

        return dto;
    }

    @Override
    public void deleteBuyerById(Long buyerId) throws NotFoundException {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow( () -> new NotFoundException("Buyer with id: " +
                buyerId + " doesn't exist"));

        buyerRepository.delete(buyer);
    }

    @Override
    public BuyerDTO addBookInBasket(Long buyerId, Long bookId) throws NotFoundException {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow( () -> new NotFoundException("Buyer with id: " +
                buyerId + " doesn't exist"));

        Basket basket = buyer.getBasket();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id: " +
                bookId + " doesn't exist"));

        basket.getBooks().add(book);

        BigDecimal totalPrice = basketService.calculateTotalPrice(basket);

        basket.setTotalPrice(totalPrice);

        buyer.setBasket(basket);

        Buyer saved = buyerRepository.save(buyer);

        List<String> bookNames = bookService.getBookNames(saved.getBasket().getBooks());

        return getBuyerDTO(saved, bookNames);

    }

    @Override
    public BuyerDTO removeBookInBasket(Long buyerId, Long bookId) throws NotFoundException {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow( () -> new NotFoundException("Buyer with id: " +
                buyerId + " doesn't exist"));

        Basket basket = buyer.getBasket();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Book with id: " +
                bookId + " doesn't exist"));

        basket.getBooks().remove(book);

        BigDecimal totalPrice = basketService.calculateTotalPrice(basket);

        basket.setTotalPrice(totalPrice);

        buyer.setBasket(basket);

        Buyer saved = buyerRepository.save(buyer);

        List<String> bookNames = bookService.getBookNames(saved.getBasket().getBooks());

        return getBuyerDTO(saved, bookNames);

    }

    private static BuyerDTO getBuyerDTO(Buyer buyer, List<String> bookNames) {
        BuyerDTO dto = BuyerDTO.builder()
                .id(buyer.getId())
                .name(buyer.getName())
                .bookNames(bookNames)
                .build();
        return dto;
    }
}
