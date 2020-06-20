package com.location.mapper.impl;

import com.location.mapper.LocationMapper;
import com.location.model.Location;
import com.product.avalability.LocationDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LocationMapperImplTest {

    private LocationMapper locationMapper;

    @Before
    public void setUp(){
        locationMapper = new LocationMapperImpl();
    }

    @Test
    public void testLocationDetailsDocWithNull(){

        assertNull(locationMapper.toLocationDoc(null));

    }

    @Test
    public void testLocationDetails(){
        final Location location = new Location();
        location.setName("test location");
        location.setId(1L);
        location.setName("irving");
        location.setZipCode("25334");

        final LocationDetails details = locationMapper.toLocationDoc(location);

        assertNotNull(details);
        assertEquals(location.getName(),details.getName());
        assertEquals(location.getName(),details.getName());
        assertEquals(location.getZipCode(),details.getZipCode());


    }
}
