package com.investobank.services;

public class BrokerCommission {

    private final long minAmount;
    private final long maxAmount;
    private final double commission;

    public BrokerCommission(long minAmount, long maxAmount, double commission) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.commission = commission;
    }

    public long getMinAmount() {
        return minAmount;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public double getCommission() {
        return commission;
    }
}
