package com.investobank.services;

public class Order {

    private final String client;
    private final long amount;
    private final OrderType orderType;

    public Order(String client, long amount, OrderType orderType) {
        this.client = client;
        this.amount = amount;
        this.orderType = orderType;
    }

    public String getClient() {
        return client;
    }

    public long getAmount() {
        return amount;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
