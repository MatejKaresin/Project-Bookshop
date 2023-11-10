package com.example.projectbookshop.services;

import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BuyerDTO;

import java.util.List;

public interface BuyerService {
    List<BuyerDTO> listAllBuyers();

    BuyerDTO createNewBuyer(BuyerDTO buyerDTO);

    void deleteBuyerById(Long buyerId) throws NotFoundException;
}
