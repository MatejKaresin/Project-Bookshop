package com.example.projectbookshop.controllers;

import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BuyerDTO;
import com.example.projectbookshop.services.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BuyerController {

    private final BuyerService buyerService;


    @Autowired
    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @GetMapping("/buyers")
    public List<BuyerDTO> getBuyers(){
        return buyerService.listAllBuyers();
    }

    @GetMapping("/buyers/{buyerId}")
    public BuyerDTO getBuyerId(@PathVariable("buyerId") Long buyerId) throws NotFoundException {
        return buyerService.getBuyerById(buyerId);

    }

    @PostMapping("/buyers")
    public BuyerDTO createBuyer(@Validated @RequestBody BuyerDTO buyerDTO){
        return buyerService.createNewBuyer(buyerDTO);
    }

    @DeleteMapping("/buyers/{buyerId}")
    public void deleteBuyer(@PathVariable("buyerId") Long buyerId) throws NotFoundException {
        buyerService.deleteBuyerById(buyerId);
    }


}
