package com.location.service;

import com.location.dao.LocationRepository;
import com.location.exception.LocationNotFoundException;
import com.location.mapper.LocationMapper;
import com.location.model.Location;
import com.product.avalability.LocationDetails;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LocationService{

    private LocationMapper locationMapper;

    private LocationRepository locationRepository;

    @Autowired
    public LocationService(final LocationMapper locationMapper,
                           final LocationRepository locationRepository){
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    public Location save(final Location location) {

        locationRepository.save(location);

        return location;
    }

    public List<LocationDetails> getAllLocations() {
        final List<LocationDetails> locationDetailsList = new ArrayList<>();
        List<Location> locations = IterableUtils.toList(locationRepository.findAll());
        if(CollectionUtils.isEmpty(locations)){
            return Collections.emptyList();
        }
        locations.forEach(location -> locationDetailsList.add(locationMapper.toLocationDoc(location)));
        return locationDetailsList;
    }

    public LocationDetails getByLocationId(final Long locationId) {

        Optional<Location> location = locationRepository.findById(locationId);
        if(location == null ||  !location.isPresent()){
            throw new LocationNotFoundException(locationId);
        }

        return locationMapper.toLocationDoc(location.get());

    }

    public void removeLocation(Long locationId) {

        locationRepository.deleteById(locationId);
    }

    public LocationDetails update(Location location, Long locationId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if(locationOptional.isPresent()){
            location.setId(locationId);
            locationRepository.save(location);
            return locationMapper.toLocationDoc(location);
        }
        return null;

    }
}
