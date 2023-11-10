package com.example.projectbookshop.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketDTO {

    private Long id;

    private String books;

    private BigDecimal totalPrice;
}
