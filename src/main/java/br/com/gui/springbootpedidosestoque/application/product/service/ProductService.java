package br.com.gui.springbootpedidosestoque.application.product.service;

import br.com.gui.springbootpedidosestoque.application.product.result.ProductResult;
import br.com.gui.springbootpedidosestoque.domain.product.Product;
import br.com.gui.springbootpedidosestoque.domain.stockmovement.MovementReasonEnum;
import br.com.gui.springbootpedidosestoque.domain.stockmovement.MovementTypeEnum;
import br.com.gui.springbootpedidosestoque.domain.stockmovement.StockMovement;
import br.com.gui.springbootpedidosestoque.infrastructure.http.request.CreateProductRequest;
import br.com.gui.springbootpedidosestoque.infrastructure.persistence.ProductRepository;
import br.com.gui.springbootpedidosestoque.infrastructure.persistence.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public ProductService(ProductRepository productRepository, StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }



    @Transactional
    public ProductResult create(CreateProductRequest product) {
        var newProduct = new Product(
                product.name(),
                product.description(),
                product.price(),
                product.stock()
        );

        var savedProduct = productRepository.save(newProduct);

        if (savedProduct.getStock() > 0) {
            var initialMovement = new StockMovement(
                    savedProduct,
                    savedProduct.getStock(),
                    MovementTypeEnum.INCOME,
                    MovementReasonEnum.CREATION
            );

            stockMovementRepository.save(initialMovement);
        }

        return from(savedProduct);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }

    private ProductResult from(Product product) {
        return new ProductResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.isActive()
        );
    }

}
