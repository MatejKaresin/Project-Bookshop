package com.example.projectbookshop.services;

import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BuyerDTO;

import java.util.List;

public interface BuyerService {
    List<BuyerDTO> listAllBuyers();

    BuyerDTO createNewBuyer(BuyerDTO buyerDTO);

    void deleteBuyerById(Long buyerId) throws NotFoundException;

    BuyerDTO getBuyerById(Long buyerId) throws NotFoundException;

    BuyerDTO addBookInBasket(Long buyerId, Long bookId) throws NotFoundException;

    BuyerDTO removeBookInBasket(Long buyerId, Long bookId) throws NotFoundException;
}
