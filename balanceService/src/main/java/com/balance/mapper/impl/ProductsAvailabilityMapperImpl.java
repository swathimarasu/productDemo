package com.balance.mapper.impl;

import com.balance.mapper.ProductsAvailabilityMapper;
import com.product.avalability.LocationDetails;
import com.product.avalability.ProductDetails;
import com.product.avalability.ProductsAvailableDoc;
import org.springframework.stereotype.Service;

@Service
public class ProductsAvailabilityMapperImpl implements ProductsAvailabilityMapper {

    @Override
    public ProductsAvailableDoc toProductsAvailabilityDoc(final LocationDetails locationDetails,
                                                          final ProductDetails productDetails,
                                                          final int balance) {

        return ProductsAvailableDoc.builder()
                .availableCount(balance)
                .location(locationDetails)
                .productDetails(productDetails)
                .build();
    }
}
