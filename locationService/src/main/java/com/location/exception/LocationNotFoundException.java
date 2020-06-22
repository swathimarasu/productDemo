package com.location.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(long locationId){

        super(String.format("Location not found for locationId %d",locationId));
    }
}
