package com.location.controller;
import com.location.model.Location;
import com.location.service.LocationService;
import com.product.avalability.LocationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController
@RequestMapping(value = "/locations")
public class LocationController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

     @Autowired
    LocationService locationService;

    @RequestMapping(value = "/create",method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public ResponseEntity<Location> saveLocation(@RequestBody Location location) {

        log.info("Saving Location details in the database.");
        return new ResponseEntity<>(locationService.save(location),HttpStatus.CREATED);
    }

    @RequestMapping(value="/getAll",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<List<LocationDetails>> getLocations(){

        log.info("retrieve Location details from the database.");
        final List<LocationDetails> locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations,HttpStatus.OK);

    }

    @RequestMapping(value="/{locationId}",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<LocationDetails>  getLocationByID(@PathVariable("locationId") final Long locationId){

        log.info("retrieve Location details from the database by locationId",locationId);
        final LocationDetails location = locationService.getByLocationId(locationId);
        return new ResponseEntity<>(location,HttpStatus.OK);

    }

    @RequestMapping(value="/update/{locationId}",method = RequestMethod.PUT)
    public ResponseEntity<LocationDetails>  updateLocation(@RequestBody final Location location,
                                                @PathVariable("locationId") final Long locationId){

        log.info("Update Location details in the database");

        return new ResponseEntity<>(locationService.update(location,locationId),HttpStatus.OK);

    }


    @RequestMapping(value="/{locationId}",method = RequestMethod.DELETE)
    public ResponseEntity<Void>  removeLocation(@PathVariable("locationId") final Long locationId){

        log.info("remove Location details from the database by locationId",locationId);
        locationService.removeLocation(locationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }

}
