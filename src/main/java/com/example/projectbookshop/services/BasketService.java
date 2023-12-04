package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BasketDTO;

import java.math.BigDecimal;
import java.util.List;

public interface BasketService {
    List<BasketDTO> listAll();

    Basket createBasket();

    BigDecimal calculateTotalPrice(Basket basket);

    BasketDTO getBasketForUser(Long userId) throws NotFoundException;
}
