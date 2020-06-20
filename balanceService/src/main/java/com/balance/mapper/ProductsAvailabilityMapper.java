package com.balance.mapper;

import com.product.avalability.LocationDetails;
import com.product.avalability.ProductDetails;
import com.product.avalability.ProductsAvailableDoc;

public interface ProductsAvailabilityMapper {

    ProductsAvailableDoc toProductsAvailabilityDoc(LocationDetails locationDetails, ProductDetails productDetails, int balance);
}
