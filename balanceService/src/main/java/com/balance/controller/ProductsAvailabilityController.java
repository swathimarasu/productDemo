package com.balance.controller;


import com.balance.model.AvailableProductEntity;
import com.balance.service.ProductsAvailabilityService;
import com.product.avalability.LocationDetails;
import com.product.avalability.ProductsAvailableDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/products/availability")
public class ProductsAvailabilityController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductsAvailabilityService productsAvailabilityService;

    @RequestMapping(value = "/create",method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public ResponseEntity<AvailableProductEntity> saveAvailableProduct(@RequestBody AvailableProductEntity availableProductEntity) {

        log.info("Saving AvailableProduct details in the database.");
        ;
        return new ResponseEntity<>(productsAvailabilityService.save(availableProductEntity),HttpStatus.CREATED);
    }

    @RequestMapping(value="/{locationId}/getAvailableProducts",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<List<ProductsAvailableDoc>> getAvailableProductsByLocationId(@PathVariable("locationId") final Long locationId){

        log.info("retrieve AvailableProduct details from the database by locationId",locationId);
        final List<ProductsAvailableDoc> availableProductsList = productsAvailabilityService.getAvailableProductsByLocId(locationId);
        return new ResponseEntity<>(availableProductsList,HttpStatus.OK);

    }


}
