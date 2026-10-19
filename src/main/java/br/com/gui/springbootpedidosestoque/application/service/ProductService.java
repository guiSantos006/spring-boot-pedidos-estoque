package br.com.gui.springbootpedidosestoque.application.service;

import br.com.gui.springbootpedidosestoque.domain.product.Product;
import br.com.gui.springbootpedidosestoque.infrastructure.http.request.CreateProductRequest;
import br.com.gui.springbootpedidosestoque.infrastructure.persistence.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Transactional
    public void create(CreateProductRequest product) {
        var newProduct = new Product(
                product.name(),
                product.description(),
                product.price(),
                product.stock()
        );

        productRepository.save(newProduct);
    }
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }
}
