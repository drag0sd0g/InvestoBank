package com.investobank.exceptions;

public class BrokerOrderAmountExceeds100Exception extends Exception {
    public BrokerOrderAmountExceeds100Exception(String message) {
        super(message);
    }
}