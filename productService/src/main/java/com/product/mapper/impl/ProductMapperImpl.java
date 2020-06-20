package com.product.mapper.impl;

import com.product.avalability.ProductDetails;
import com.product.mapper.ProductMapper;
import com.product.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDetails toProductDetailDoc(Product product) {
        if(product == null){
            return  null;
        }

        return ProductDetails.builder()
                .deptId(product.getDeptId())
                .deptName(product.getDeptName())
                .id(product.getId())
                .name(product.getName())
                .build();

    }
}
