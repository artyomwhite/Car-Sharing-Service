package com.champions.carsharingservice.exception;

public class RentalNotActiveException extends RuntimeException {
    public RentalNotActiveException(String message) {
        super(message);
    }
}
