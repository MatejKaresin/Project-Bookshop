package com.example.projectbookshop.controllers;

import com.example.projectbookshop.model.BasketDTO;
import com.example.projectbookshop.services.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
