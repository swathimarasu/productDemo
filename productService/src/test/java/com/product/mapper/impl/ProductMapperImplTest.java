package com.product.mapper.impl;


import com.product.avalability.ProductDetails;
import com.product.mapper.ProductMapper;
import com.product.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProductMapperImplTest {

    private ProductMapper productMapper;

    @Before
    public void setUp(){
        productMapper = new ProductMapperImpl();
    }

    @Test
    public void testProductDetailsDocWithNull(){

        assertNull(productMapper.toProductDetailDoc(null));

    }

    @Test
    public void testProductDetails(){
        final Product product = new Product();
        product.setName("test Product");
        product.setId(1L);
        product.setDeptId(2L);
        product.setDeptName("test dept");

        final ProductDetails details = productMapper.toProductDetailDoc(product);

        assertNotNull(details);
        assertEquals(product.getName(),details.getName());
        assertEquals(product.getDeptId(),details.getDeptId());
        assertEquals(product.getDeptName(),details.getDeptName());


    }


}
