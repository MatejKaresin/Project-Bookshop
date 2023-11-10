package com.example.projectbookshop.services;

import com.example.projectbookshop.entities.Basket;
import com.example.projectbookshop.entities.Buyer;
import com.example.projectbookshop.exceptions.NotFoundException;
import com.example.projectbookshop.model.BuyerDTO;
import com.example.projectbookshop.repositories.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuyerServiceImpl implements BuyerService {


    private final BuyerRepository buyerRepository;
    private final BasketService basketService;


    @Autowired
    public BuyerServiceImpl(BuyerRepository buyerRepository, BasketService basketService) {
        this.buyerRepository = buyerRepository;
        this.basketService = basketService;
    }

    @Override
    public List<BuyerDTO> listAllBuyers() {
        List<Buyer> buyers = buyerRepository.findAll();

        List<BuyerDTO> dtos = new ArrayList<>();

        for(Buyer buyer : buyers){
            BuyerDTO dto = BuyerDTO.builder()
                    .id(buyer.getId())
                    .name(buyer.getName())
                    .basketId(buyer.getBasket().getId())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public BuyerDTO getBuyerById(Long buyerId) throws NotFoundException {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow( () -> new NotFoundException("Buyer with id: " +
                buyerId + " doesn't exist"));

        BuyerDTO dto = BuyerDTO.builder()
                .id(buyer.getId())
                .name(buyer.getName())
                .basketId(buyer.getBasket().getId())
                .build();

        return dto;
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
                .basketId(buyer.getBasket().getId())
                .build();

        return dto;
    }

    @Override
    public void deleteBuyerById(Long buyerId) throws NotFoundException {
        Buyer buyer = buyerRepository.findById(buyerId).orElseThrow( () -> new NotFoundException("Buyer with id: " +
                buyerId + " doesn't exist"));

        buyerRepository.delete(buyer);
    }
}
