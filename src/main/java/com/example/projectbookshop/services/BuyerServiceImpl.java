package com.example.projectbookshop.services;

import com.example.projectbookshop.controllers.BuyerController;
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


    @Autowired
    public BuyerServiceImpl(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    @Override
    public List<BuyerDTO> listAllBuyers() {
        List<Buyer> buyers = buyerRepository.findAll();

        List<BuyerDTO> dtos = new ArrayList<>();

        for(Buyer buyer : buyers){
            BuyerDTO dto = BuyerDTO.builder()
                    .id(buyer.getId())
                    .name(buyer.getName())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public BuyerDTO createNewBuyer(BuyerDTO buyerDTO) {
        Buyer buyer = Buyer.builder()
                .name(buyerDTO.getName())
                .build();

        Buyer saved = buyerRepository.save(buyer);

        BuyerDTO dto = BuyerDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
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
