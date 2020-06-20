package com.product.mapper;

import com.product.avalability.ProductDetails;
import com.product.model.Product;

public interface ProductMapper {


    ProductDetails toProductDetailDoc(Product product);
}
