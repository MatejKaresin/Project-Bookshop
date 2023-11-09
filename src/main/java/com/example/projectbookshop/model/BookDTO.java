package com.example.projectbookshop.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long id;
    @NotEmpty(message = "Name can't be empty")
    private String name;
    @NotNull(message = "Pages can't be null")
    private Integer numberOfPages;
    @NotNull(message = "Price can't be null")
    private BigDecimal price;
    private String authorFullName;
}
