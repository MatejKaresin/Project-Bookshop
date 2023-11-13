package com.example.projectbookshop.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyerDTO {

    private Long id;

    @NotEmpty(message = "Name can't be empty")
    private String name;

    private List<String> bookNames;
}
