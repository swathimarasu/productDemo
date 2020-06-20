package com.balance.mapper.impl;

import com.balance.mapper.ProductsAvailabilityMapper;
import com.product.avalability.LocationDetails;
import com.product.avalability.ProductDetails;
import com.product.avalability.ProductsAvailableDoc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProductsAvailabilityMapperImplTest {

    private ProductsAvailabilityMapper productsAvailabilityMapper;

    @Before
    public void setUp(){
        productsAvailabilityMapper = new ProductsAvailabilityMapperImpl();
    }


    @Test
    public void testAvailableProductDetails(){

        final LocationDetails locationDetails = LocationDetails.builder()
                .id(1L)
                .name("Irving")
                .zipCode("75063")
                .build();

        final ProductDetails productDetails = ProductDetails.builder()
                .deptId(2L)
                .name("Shirts")
                .id(1L).deptName("Sleeves")
                .build();
        final int balance = 10;

        final ProductsAvailableDoc result = productsAvailabilityMapper.toProductsAvailabilityDoc(locationDetails, productDetails,balance);

        assertNotNull(result);
        assertEquals(locationDetails,result.getLocation());
        assertEquals(productDetails,result.getProductDetails());
        assertEquals(balance,result.getAvailableCount());
    }
}
