package com.location.controller;

import com.location.Application;
import com.location.model.Location;
import com.product.avalability.LocationDetails;
import com.product.avalability.ProductDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControllerIT {

    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();


    @Test
    public void testNoLocationFound_ShouldReturnHttpStatusCode404() throws Exception {

        final Long locationId = 1234L;

        HttpEntity<LocationDetails> entity = new HttpEntity<LocationDetails>(null, headers);

        ResponseEntity<LocationDetails> response = restTemplate.exchange(
                createURLWithPort("/locations/" + locationId),
                HttpMethod.GET, entity, LocationDetails.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }


    @Test
    public void testCreateLocation() {

        Location location = new Location();
        location.setId(1234);
        location.setZipCode("23245");
        location.setName("irving");

        HttpEntity<Location> entity = new HttpEntity<Location>(location, headers);
        ResponseEntity<Location> response = restTemplate.exchange(
                createURLWithPort("/locations/create"),
                HttpMethod.POST, entity, Location.class);

        assertNotNull(response.getBody());
        assertEquals(location.getZipCode(), response.getBody().getZipCode());
    }


    private String createURLWithPort(String uri) {

        return "http://localhost:" + port + uri;

    }
}
