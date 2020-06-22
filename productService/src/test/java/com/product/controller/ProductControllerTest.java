package com.product.controller;


import com.product.avalability.ProductDetails;
import com.product.avalability.ProductsDetailsListResponse;
import com.product.model.Product;
import com.product.service.ProductService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    private static EasyRandom easyRandom;
    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Before
    public  void oneTimeSetUp(){

        final EasyRandomParameters parameters = new EasyRandomParameters()
                .collectionSizeRange(2, 3)
                .stringLengthRange(5, 10)
                .charset(Charset.forName("UTF-8"))
                .dateRange(LocalDate.now().minusDays(7), LocalDate.now())
                .timeRange(LocalTime.MIN, LocalTime.MAX)
                .scanClasspathForConcreteTypes(true)
                .objectPoolSize(20)
                .randomizationDepth(3)
                .overrideDefaultInitialization(false)
                .ignoreRandomizationErrors(true);

        easyRandom = new EasyRandom(parameters);

        productController = new ProductController(productService);
    }


    @Test
    public void testGetProducts(){

        final List<ProductDetails> products = Arrays.asList(easyRandom.nextObject(ProductDetails[].class));
        when(productService.getAllProducts()).thenReturn(products);

        final ResponseEntity<List<ProductDetails>> result = productController.getProducts();

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(products,result.getBody());
    }

    @Test
    public void testGetProductById(){

        final long productId = easyRandom.nextLong();

        final ProductDetails product = easyRandom.nextObject(ProductDetails.class);
        when(productService.getByProductId(anyLong())).thenReturn(product);

        final ResponseEntity<ProductDetails> result = productController.getProductByID(productId);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(product,result.getBody());
    }

    @Test
    public void testSaveProduct(){

        final Product product = easyRandom.nextObject(Product.class);
        when(productService.save(any())).thenReturn(product);

        final ResponseEntity<Product> result = productController.saveProduct(product);

        assertEquals(HttpStatus.CREATED,result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(product,result.getBody());
    }

    @Test
    public void testDeleteProduct(){

        final Long productId = easyRandom.nextLong();

        doNothing().when(productService).removeProductById(anyLong());

        final ResponseEntity<Void> result = productController.removeProduct(productId);

        assertEquals(HttpStatus.NO_CONTENT,result.getStatusCode());


    }

    @Test
    public void testUpdateProduct(){

        final Long productId = easyRandom.nextLong();
        final Product product = easyRandom.nextObject(Product.class);
        final ProductDetails productDetails = easyRandom.nextObject(ProductDetails.class);

        when(productService.update(any(),anyLong())).thenReturn(productDetails);

        final ResponseEntity<ProductDetails> result = productController.updateProduct(product,productId);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(productDetails,result.getBody());
    }

    @Test
    public void testGetProductDetailsList(){

        final String itemName = easyRandom.nextObject(String.class);
        final String deptName = easyRandom.nextObject(String.class);

        final ProductsDetailsListResponse productDetails = easyRandom.nextObject(ProductsDetailsListResponse.class);

        when(productService.getProductsDetailsList(anyString(),anyString())).thenReturn(productDetails);

        final ResponseEntity<ProductsDetailsListResponse> result = productController.getProductDetailsList(itemName,deptName);
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(productDetails,result.getBody());


    }

}
