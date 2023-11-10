package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.model.BasketDTO;

import java.util.List;

public interface BasketService {
    List<BasketDTO> listAll();

    Basket createBasket();
}
