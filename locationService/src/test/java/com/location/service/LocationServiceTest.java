package com.location.service;

import com.location.exception.LocationNotFoundException;
import com.product.avalability.LocationDetails;
import com.location.dao.LocationRepository;
import com.location.mapper.LocationMapper;
import com.location.model.Location;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.*;


@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

    private static EasyRandom easyRandom;
    private LocationService locationService;

    @Mock
    private LocationMapper locationMapper;

    @Mock
    private LocationRepository locationRepository;

    @Before
    public void setUp(){
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
        locationService = new LocationService(locationMapper,locationRepository);
    }


    @Test
    public void testGetLocations(){

        final Iterable<Location> locations = Arrays.asList(easyRandom.nextObject(Location[].class));

        final int size = getSize(locations);

        final LocationDetails locationDetails = easyRandom.nextObject(LocationDetails.class);

        when(locationRepository.findAll()).thenReturn(locations);
        when(locationMapper.toLocationDoc(any())).thenReturn(locationDetails);

        final List<LocationDetails> result = locationService.getAllLocations();

        assertNotNull(result);
        assertEquals(size,result.size());
    }

    @Test
    public void testNoLocations(){

        when(locationRepository.findAll()).thenReturn(new ArrayList<>());

        locationService.getAllLocations();

        verify(locationMapper,never()).toLocationDoc(any());
    }


    @Test
    public void testGetLocationById(){

        final Location location = easyRandom.nextObject(Location.class);
        final Long locationId = easyRandom.nextLong();
        location.setId(locationId);
        location.setId(2L);
        location.setName("Shirts");
        location.setZipCode("123422");

        final LocationDetails locationDetails = easyRandom.nextObject(LocationDetails.class);
        locationDetails.setName(location.getName());
        locationDetails.setId(locationId);
        locationDetails.setZipCode(location.getZipCode());

        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(locationMapper.toLocationDoc(any())).thenReturn(locationDetails);

        final LocationDetails details = locationService.getByLocationId(locationId);

        assertNotNull(details);
        assertEquals(locationDetails.getId(),details.getId());
        assertEquals(location.getName(),details.getName());

    }

    @Test(expected = LocationNotFoundException.class)
    public void testGetLocationByIdWithNull(){

        final Location location = easyRandom.nextObject(Location.class);
        final Long locationId = easyRandom.nextLong();
        location.setId(locationId);
        location.setId(2L);
        location.setName("Shirts");
        location.setZipCode("12343");


        when(locationRepository.findById(anyLong())).thenReturn(null);

        locationService.getByLocationId(locationId);

         verify(locationMapper,never()).toLocationDoc(any());

}

    @Test
    public void testSaveLocation(){

        final Location location = easyRandom.nextObject(Location.class);
        when(locationRepository.save(location)).thenReturn(location);

        final Location result = locationService.save(location);

        assertNotNull(result);
        assertEquals(result ,location);
    }

    @Test
    public void testDeleteLocation(){

        final Long locationId = easyRandom.nextLong();
        final Location product = easyRandom.nextObject(Location.class);
        doNothing().when(locationRepository).deleteById(anyLong());

        locationService.removeLocation(locationId);

        verify(locationRepository).deleteById(anyLong());


    }

    @Test
    public void testUpdateLocation(){

        final long locationId = easyRandom.nextLong();
        final Location product = easyRandom.nextObject(Location.class);
        final LocationDetails details = easyRandom.nextObject(LocationDetails.class);
        details.setId(locationId);

        when(locationRepository.findById(locationId)).thenReturn(Optional.of(product));

        when(locationMapper.toLocationDoc(any())).thenReturn(details);

        final LocationDetails result = locationService.update(product,locationId);

        assertNotNull(result);
        assertEquals(locationId,result.getId());

    }

    @Test
    public void testUpdateProductWithNoEntity(){

        final Long locationId = easyRandom.nextLong();
        final Location location = easyRandom.nextObject(Location.class);
        final LocationDetails details = easyRandom.nextObject(LocationDetails.class);
        details.setId(locationId);

        when(locationRepository.findById(locationId)).thenReturn(Optional.ofNullable(null));

        final LocationDetails result = locationService.update(location,locationId);

        assertNull(result);

    }



    private int getSize(Iterable<Location> locations) {
        if (locations instanceof Collection<?>) {
            return ((Collection<?>) locations).size();
        }

        return 0;


    }
}
