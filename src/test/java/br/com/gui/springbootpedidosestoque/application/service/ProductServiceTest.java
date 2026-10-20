package br.com.gui.springbootpedidosestoque.application.service;

import br.com.gui.springbootpedidosestoque.application.product.service.ProductService;
import br.com.gui.springbootpedidosestoque.domain.product.Product;
import br.com.gui.springbootpedidosestoque.domain.stockmovement.MovementReasonEnum;
import br.com.gui.springbootpedidosestoque.domain.stockmovement.MovementTypeEnum;
import br.com.gui.springbootpedidosestoque.domain.stockmovement.StockMovement;
import br.com.gui.springbootpedidosestoque.infrastructure.http.request.CreateProductRequest;
import br.com.gui.springbootpedidosestoque.infrastructure.persistence.ProductRepository;
import br.com.gui.springbootpedidosestoque.infrastructure.persistence.StockMovementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldSaveProductAndRegisterInitialStockMovement() {
        var request = new CreateProductRequest(
                "Teclado",
                "Teclado mecânico",
                new BigDecimal("199.90"),
                10L
        );
        var generatedId = UUID.randomUUID();

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(generatedId);
            return p;
        });

        var result = productService.create(request);

        // Produto salvo com os dados do request
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertEquals("Teclado", savedProduct.getName());
        assertEquals("Teclado mecânico", savedProduct.getDescription());
        assertEquals(new BigDecimal("199.90"), savedProduct.getPrice());
        assertEquals(10L, savedProduct.getStock());

        // Movimentação inicial registrada
        ArgumentCaptor<StockMovement> movementCaptor = ArgumentCaptor.forClass(StockMovement.class);
        verify(stockMovementRepository).save(movementCaptor.capture());

        StockMovement movement = movementCaptor.getValue();
        assertSame(savedProduct, movement.getProduct());
        assertEquals(10L, movement.getQuantity());
        assertEquals(MovementTypeEnum.INCOME, movement.getType());
        assertEquals(MovementReasonEnum.CREATION, movement.getReason());

        // Resultado devolvido
        assertEquals(generatedId, result.id());
        assertEquals("Teclado", result.name());
        assertEquals(10L, result.stock());
        assertTrue(savedProduct.isActive());
    }

    @Test
    void shouldNotRegisterStockMovementWhenInitialStockIsZero() {
        var request = new CreateProductRequest(
                "Mouse",
                "Mouse sem fio",
                new BigDecimal("89.90"),
                0L
        );

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.create(request);

        verify(productRepository).save(any(Product.class));
        verifyNoInteractions(stockMovementRepository);
    }
}
