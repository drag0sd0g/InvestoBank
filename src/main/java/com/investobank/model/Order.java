package com.investobank.model;

import com.investobank.services.BrokerServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final String client;
    private final long amount;

    public Order(String client, long amount) {
        this.client = client;
        this.amount = amount;
    }

    public String getClient() {
        return client;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isBuy(){
        return amount > 0;
    }

    public List<Order> split(){
        List<Order> splitOrders = new ArrayList<>();
        long totalAmount = Math.abs(amount);
        while(totalAmount > 0){
            if(totalAmount - BrokerServiceImpl.MAX_DIGICOIN_ORDER_LIMIT > 0) {
                splitOrders.add(new Order(client, isBuy() ? BrokerServiceImpl.MAX_DIGICOIN_ORDER_LIMIT :
                        -BrokerServiceImpl.MAX_DIGICOIN_ORDER_LIMIT));
                totalAmount -= BrokerServiceImpl.MAX_DIGICOIN_ORDER_LIMIT;
            } else {
                splitOrders.add(new Order(client, isBuy() ? totalAmount : -totalAmount));
                break;
            }
        }
        return splitOrders;
    }
}
