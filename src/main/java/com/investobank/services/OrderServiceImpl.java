package com.investobank.services;

import com.sun.corba.se.pept.broker.Broker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    private final Map<String, List<Order>> inMemoryOrderAudit;

    private final List<Broker> brokers;

    public OrderServiceImpl(List<Broker> brokers) {
        this.brokers = Collections.unmodifiableList(brokers);
        this.inMemoryOrderAudit = new HashMap<>();
    }

    @Override
    public void executeOrder(Order order) throws OrderNotMultipleOf10Exception {
        if(order.getAmount() % 10 != 0){
            throw new OrderNotMultipleOf10Exception("Order amount not a multiple of 10 but "+order.getAmount());
        }


    }
}
