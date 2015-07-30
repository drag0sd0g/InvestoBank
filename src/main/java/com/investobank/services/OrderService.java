package com.investobank.services;

public interface OrderService {
    void executeOrder(Order order) throws OrderNotMultipleOf10Exception;
}
