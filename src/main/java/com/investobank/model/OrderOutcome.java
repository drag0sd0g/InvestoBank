package com.investobank.model;

public class OrderOutcome {

    private final Order order;
    private final double price;

    public OrderOutcome(Order order, double price) {
        this.order = order;
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public double getPrice() {
        return price;
    }
}
