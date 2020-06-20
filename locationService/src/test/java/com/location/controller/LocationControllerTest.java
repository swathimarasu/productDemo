package com.location.controller;

import com.product.avalability.LocationDetails;
import com.location.model.Location;
import com.location.service.LocationService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.BeforeClass;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LocationControllerTest {

    private static EasyRandom easyRandom;
    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationService locationService;

    @BeforeClass
    public static void oneTimeSetUp(){

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
    }


    @Test
    public void testGetLocations() {

        final List<LocationDetails> products = Arrays.asList(easyRandom.nextObject(LocationDetails[].class));
        when(locationService.getAllLocations()).thenReturn(products);

        final ResponseEntity<List<LocationDetails>> result = locationController.getLocations();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(products, result.getBody());
    }



    @Test
    public void testGetLocationById(){

        final long locationId = easyRandom.nextLong();

        final LocationDetails location = easyRandom.nextObject(LocationDetails.class);
        when(locationService.getByLocationId(anyLong())).thenReturn(location);

        final ResponseEntity<LocationDetails> result = locationController.getLocationByID(locationId);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(location,result.getBody());
    }


    @Test
    public void testSaveLocation(){

        final Location location = easyRandom.nextObject(Location.class);
        when(locationService.save(any())).thenReturn(location);

        final ResponseEntity<Location> result = locationController.saveLocation(location);

        assertEquals(HttpStatus.CREATED,result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(location,result.getBody());
    }


    @Test
    public void testDeleteLocation(){

        final Long locationId = easyRandom.nextLong();

        doNothing().when(locationService).removeLocation(anyLong());

        final ResponseEntity<Void> result = locationController.removeLocation(locationId);

        assertEquals(HttpStatus.NO_CONTENT,result.getStatusCode());


    }


    @Test
    public void testUpdateLocation(){

        final Long locationId = easyRandom.nextLong();
        final Location location = easyRandom.nextObject(Location.class);
        final LocationDetails locationDetails = easyRandom.nextObject(LocationDetails.class);

        when(locationService.update(any(),anyLong())).thenReturn(locationDetails);

        final ResponseEntity<LocationDetails> result = locationController.updateLocation(location,locationId);

        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(locationDetails,result.getBody());
    }

}
