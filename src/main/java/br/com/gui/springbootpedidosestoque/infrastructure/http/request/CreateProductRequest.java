package br.com.gui.springbootpedidosestoque.infrastructure.http.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(

        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 255)
        String description,

        @NotNull
        @PositiveOrZero
        @Digits(integer = 17, fraction = 2)
        BigDecimal price,

        @NotNull
        @PositiveOrZero
        Long stock) {
}