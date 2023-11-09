package com.example.projectbookshop.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long id;
    private String name;
    private Integer numberOfPages;
    private BigDecimal price;
    private String authorFullName;
}
