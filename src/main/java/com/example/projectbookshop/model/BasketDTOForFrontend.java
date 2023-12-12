package com.example.projectbookshop.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketDTOForFrontend {

    private Long id;
    private List<Long> bookIds;
}
