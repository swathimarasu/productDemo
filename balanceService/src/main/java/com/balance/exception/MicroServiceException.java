package com.balance.exception;



public class MicroServiceException extends RuntimeException {

    public MicroServiceException(String message){

        super(message);
    }
}
