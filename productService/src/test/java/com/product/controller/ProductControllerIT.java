package com.product.controller;

import com.product.Application;
import com.product.avalability.ProductDetails;
import com.product.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIT {

    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testNoProductFound_ShouldReturnHttpStatusCode404() throws Exception {

        final Long productId = 1234L;

        HttpEntity<ProductDetails> entity = new HttpEntity<ProductDetails>(null, headers);

        ResponseEntity<ProductDetails> response = restTemplate.exchange(
                createURLWithPort("/products/" + productId),
                HttpMethod.GET, entity, ProductDetails.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void testCreateProduct() throws Exception {

        Product product = new Product();
        product.setId(1234);
        product.setDeptId(2L);
        product.setDeptName("Shirts");
        product.setName("Sleeves");

        HttpEntity<Product> entity = new HttpEntity<Product>(product, headers);
        ResponseEntity<Product> response = restTemplate.exchange(
                createURLWithPort("/products/create"),
                HttpMethod.POST, entity, Product.class);

        assertNotNull(response.getBody());
        assertEquals(product.getDeptId(),response.getBody().getDeptId());

    }

    @Test
    public void testGetProductByID() throws Exception {

        final Long productId = 1234L;

        HttpEntity<ProductDetails> entity = new HttpEntity<ProductDetails>(null, headers);

        ResponseEntity<ProductDetails> response = restTemplate.exchange(
                createURLWithPort("/products/"+productId),
                HttpMethod.GET, entity, ProductDetails.class);

        assertNotNull(response.getBody());
        assertEquals(productId.longValue(),response.getBody().getId().longValue());
        assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    private String createURLWithPort(String uri) {

        return "http://localhost:" + port + uri;

    }


}
