package com.investobank.model;

public class FixedBrokerComission implements BrokerCommission {
    private final double commission;

    public FixedBrokerComission(double commission) {
        this.commission = commission;
    }

    public double getCommission() {
        return commission;
    }

    @Override
    public double calculateCommission(long amount) {
        return commission;
    }
}
