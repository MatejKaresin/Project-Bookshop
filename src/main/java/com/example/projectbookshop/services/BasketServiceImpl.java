package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Book;
import com.example.projectbookshop.model.BasketDTO;
import com.example.projectbookshop.repositories.BasketRepository;
import org.hibernate.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
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
}
