package com.balance.exception;



public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(){

    }

    public ProductNotFoundException(long productId){
        super(String.format("Location not found for %d",productId));
    }
}
