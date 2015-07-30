package com.investobank.model;

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
}
