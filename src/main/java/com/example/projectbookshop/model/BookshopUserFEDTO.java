package com.example.projectbookshop.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookshopUserFEDTO {

    private Long id;

    @NotEmpty(message = "Nickname can't be empty")
    private String nickName;
    private Boolean logged;
}
