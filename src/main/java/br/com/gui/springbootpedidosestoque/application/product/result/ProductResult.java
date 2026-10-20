package br.com.gui.springbootpedidosestoque.application.product.result;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResult(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Long stock,
        boolean active
) {
}