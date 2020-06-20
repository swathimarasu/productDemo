package com.balance.controller;


import com.balance.Application;
import com.balance.model.AvailableProductEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

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


    private String createURLWithPort(String uri) {

        return "http://localhost:" + port + uri;

    }

}


