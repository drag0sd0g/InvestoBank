package com.investobank.services;

import com.investobank.exceptions.BrokerOrderAmountExceeds100Exception;
import com.investobank.exceptions.OrderNotMultipleOf10Exception;
import com.investobank.exceptions.ValidCommissionNotFoundException;
import com.investobank.model.BrokerCommission;
import com.investobank.model.Order;
import com.investobank.model.VariableBrokerComission;

import java.util.Collections;
import java.util.List;

public class BrokerServiceImpl implements BrokerService {

    private final List<BrokerCommission> brokerCommissions;

    private final double digicoinQuote;

    private final String name;

    public BrokerServiceImpl(String name, List<BrokerCommission> brokerCommissions, double digicoinQuote) {
        this.name = name;
        this.brokerCommissions = Collections.unmodifiableList(brokerCommissions);
        this.digicoinQuote = digicoinQuote;
    }

    @Override
    public double getQuote(Order order) throws OrderNotMultipleOf10Exception, BrokerOrderAmountExceeds100Exception, ValidCommissionNotFoundException {
        if(order.getAmount() % 10 != 0){
            throw new OrderNotMultipleOf10Exception("Order amount not a multiple of 10 but "+order.getAmount());
        }

        if(Math.abs(order.getAmount()) > 100){
            throw new BrokerOrderAmountExceeds100Exception("Order amount bigger than 100. Actual amount: "+order.getAmount());
        }

        double targetCommissionPercentile = Integer.MIN_VALUE;
        for(BrokerCommission brokerCommission : brokerCommissions){
            double interimCommissionPercentile = brokerCommission.calculateCommission(Math.abs(order.getAmount()));
            if(interimCommissionPercentile != VariableBrokerComission.COMMISSION_NOT_APPLICABLE_FOR_THIS_RANGE){
                targetCommissionPercentile = interimCommissionPercentile;
                break;
            }
        }

        if(targetCommissionPercentile == Integer.MIN_VALUE){
            throw  new ValidCommissionNotFoundException("could not find any applicable rules for determining the right commission");
        }

        double transactionTotalNoCommission = Math.abs(order.getAmount()) * digicoinQuote;
        return transactionTotalNoCommission + ((targetCommissionPercentile / 100) * transactionTotalNoCommission);
    }

    @Override
    public String getName() {
        return name;
    }
}
