package com.product.service;


import com.product.avalability.ProductDetails;
import com.product.avalability.ProductsDetailsListResponse;
import com.product.dao.ProductRepository;
import com.product.exception.ProductNotFoundException;
import com.product.mapper.ProductMapper;
import com.product.model.Product;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    public ProductsDetailsListResponse getProductsDetailsList(final String name, final String deptName) {

        final List<Product> products = getProducts(name, deptName);
        if(CollectionUtils.isEmpty(products)){
          return ProductsDetailsListResponse.builder()
                  .productDetails(Collections.emptyList())
                  .build();
      }
      return ProductsDetailsListResponse.builder().productDetails(products.stream()
              .filter(Objects::nonNull)
              .map(product -> productMapper.toProductDetailDoc(product))
              .collect(Collectors.toList())).build();

    }


    private List<Product> getProducts(String name, String deptName) {

        if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(deptName)){
            return productRepository.findByProductAndDeptNames(name,deptName);

        }else if(StringUtils.isNotBlank(name)){
            return productRepository.findByProductName(name);

        }else if(StringUtils.isNotBlank(deptName)){
            return productRepository.findByDeptName(deptName);
        }
        return Collections.emptyList();
    }
}
