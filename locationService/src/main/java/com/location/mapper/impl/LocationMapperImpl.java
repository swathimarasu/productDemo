package com.location.mapper.impl;

import com.location.mapper.LocationMapper;
import com.location.model.Location;
import com.product.avalability.LocationDetails;
import org.springframework.stereotype.Service;

@Service
public class LocationMapperImpl implements LocationMapper {
    @Override
    public LocationDetails toLocationDoc(Location location) {

        if(location == null){
            return null;
        }

        return LocationDetails.builder()
                .id(location.getId())
                .name(location.getName())
                .zipCode(location.getZipCode())
                .build();

    }
}
