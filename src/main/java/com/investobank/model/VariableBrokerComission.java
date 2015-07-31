package com.investobank.model;

public class VariableBrokerComission implements BrokerCommission {

    public static final double COMMISSION_NOT_APPLICABLE_FOR_THIS_RANGE = -1.0;

    private final long minAmount;
    private final long maxAmount;
    private final double commission;

    public VariableBrokerComission(long minAmount, long maxAmount, double commission) {
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

    @Override
    public double calculateCommission(long amount) {
        return getMinAmount() <= amount && amount <= getMaxAmount() ?
                getCommission() : COMMISSION_NOT_APPLICABLE_FOR_THIS_RANGE;
    }
}
