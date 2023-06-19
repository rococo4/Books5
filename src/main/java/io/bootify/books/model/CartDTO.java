package io.bootify.books.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartDTO {

    private Long id;

    @NotNull
    private Integer quantity;

    private Long books;

}
