package com.example.projectbookshop.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer numberOfPages;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
