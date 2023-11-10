package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.model.BasketDTO;
import com.example.projectbookshop.repositories.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            BasketDTO dto = BasketDTO.builder()
                    .id(basket.getId())
                    .books(basket.getBooks())
                    .totalPrice(basket.getTotalPrice())
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
}
