package com.investobank.services;

public class OrderNotMultipleOf10Exception extends Exception {
    public OrderNotMultipleOf10Exception(String message) {
        super(message);
    }
}
