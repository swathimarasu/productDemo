package com.product.service;


import com.product.avalability.ProductDetails;
import com.product.dao.ProductRepository;
import com.product.exception.ProductNotFoundException;
import com.product.mapper.ProductMapper;
import com.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(final ProductMapper productMapper,
                          final ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }

    public Product save(final Product product) {
        productRepository.save(product);
        return product;
    }

    public List<ProductDetails> getAllProducts() {
        final List<ProductDetails> products = new ArrayList<>();
        productRepository.findAll().forEach(product -> products.add(productMapper.toProductDetailDoc(product)));
        return products;
    }

    public ProductDetails getByProductId(final Long productId) {

        Optional<Product> product = productRepository.findById(productId);
        if (product == null || !product.isPresent()) {
            throw new ProductNotFoundException(productId);
        }

        return productMapper.toProductDetailDoc(product.get());


    }

    public void removeProductById(Long productId) {

        productRepository.deleteById(productId);
    }

    public ProductDetails update(Product product, Long productId) {
        Optional<Product> locationOptional = productRepository.findById(productId);
        if (locationOptional.isPresent()) {
            product.setId(productId);
            return productMapper.toProductDetailDoc(save(product));
        }
        return null;

    }

}
