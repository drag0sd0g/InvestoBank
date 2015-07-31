package com.investobank.model;

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
            if(totalAmount - 100 > 0) {
                splitOrders.add(new Order(client, isBuy() ? 100 : -100));
                totalAmount -= 100;
            } else {
                splitOrders.add(new Order(client, isBuy() ? totalAmount : -totalAmount));
                break;
            }
        }
        return splitOrders;
    }
}
