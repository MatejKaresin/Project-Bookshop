package com.example.projectbookshop.controllers;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BasketDTO;
import com.example.projectbookshop.model.BasketDTOForFrontend;
import com.example.projectbookshop.services.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BasketController {

    private final BasketService basketService;


    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/baskets")
    public List<BasketDTO> getAllBaskets(){
        return basketService.listAll();
    }

    @GetMapping("/users/{userId}/basketCheckout")
    public BasketDTO getUsersCheckout(@PathVariable("userId") Long userId) throws NotFoundException {
        return basketService.getCheckoutForUser(userId);
    }

    @GetMapping("/users/{userId}/basket")
    public BasketDTOForFrontend getUsersBasket(@PathVariable("userId") Long userId) throws NotFoundException {
        return basketService.getBasketForUser(userId);
    }

}
