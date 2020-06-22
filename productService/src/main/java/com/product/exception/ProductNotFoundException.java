package com.product.exception;



public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(long productId){

        super(String.format("Product not found for productId %d",productId));
    }
}
