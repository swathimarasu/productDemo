package com.balance.controller;


import com.balance.Application;
import com.balance.model.AvailableProductEntity;
import com.product.avalability.ProductsAvailableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import  java.util.*;

import static org.junit.Assert.*;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ProductsAvailabilityControllerIT {

    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();




    @Test
    public void testCreateAvailableProduct() throws Exception {

        AvailableProductEntity availableProductEntity = new AvailableProductEntity();
        availableProductEntity.setLocationId(123);
        availableProductEntity.setProductId(1234);
        availableProductEntity.setBalance(20);

        HttpEntity<AvailableProductEntity> entity = new HttpEntity<AvailableProductEntity>(availableProductEntity, headers);
        ResponseEntity<AvailableProductEntity> response = restTemplate.exchange(
                createURLWithPort("/products/availability/create"),
                HttpMethod.POST, entity, AvailableProductEntity.class);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(availableProductEntity.getBalance(),response.getBody().getBalance());

    }

    @Test
    public void testInvalidDataExceptionForGetAvailableProducts(){


        HttpEntity entity = new HttpEntity<AvailableProductEntity>(headers);
        ResponseEntity<ProductsAvailableResponse> response = restTemplate.exchange(
                createURLWithPort("/products/availability/list"),
                HttpMethod.GET, entity, ProductsAvailableResponse.class);

        assertNull(response.getBody().getProductsAvailableDocs());
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }



    private String createURLWithPort(String uri) {

        return "http://localhost:" + port + uri;

    }

}


