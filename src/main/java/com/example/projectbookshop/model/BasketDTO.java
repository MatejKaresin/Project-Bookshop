package com.example.projectbookshop.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketDTO {

    private Long id;

    private List<String> bookNames;

    private BigDecimal totalPrice;
}
