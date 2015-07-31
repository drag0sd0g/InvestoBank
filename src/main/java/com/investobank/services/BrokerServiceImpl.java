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

    public static final int MAX_DIGICOIN_ORDER_LIMIT = 100;

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
        //check division by 10
        if(order.getAmount() % OrderServiceImpl.ORDER_MULTIPLE_FACTOR != 0){
            throw new OrderNotMultipleOf10Exception("Order amount not a multiple of 10 but "+order.getAmount());
        }

        //check max allowed amount
        if(Math.abs(order.getAmount()) > MAX_DIGICOIN_ORDER_LIMIT){
            throw new BrokerOrderAmountExceeds100Exception("Order amount bigger than 100. Actual amount: "+order.getAmount());
        }

        //calculate commission with respect to input and pre-defined rules
        double targetCommissionPercentile = Integer.MIN_VALUE;
        for(BrokerCommission brokerCommission : brokerCommissions){
            double interimCommissionPercentile = brokerCommission.decideCommission(Math.abs(order.getAmount()));
            if(interimCommissionPercentile != VariableBrokerComission.COMMISSION_NOT_APPLICABLE_FOR_THIS_RANGE){
                targetCommissionPercentile = interimCommissionPercentile;
                break;
            }
        }

        //edge case but still possible if the variable broker commissions are not properly defined
        if(targetCommissionPercentile == Integer.MIN_VALUE){
            throw  new ValidCommissionNotFoundException("could not find any applicable rules for determining the right commission");
        }

        //calculate and return total cost for this order by this broker
        double transactionTotalNoCommission = Math.abs(order.getAmount()) * digicoinQuote;
        double commissionAddon = (targetCommissionPercentile / MAX_DIGICOIN_ORDER_LIMIT) * transactionTotalNoCommission;
        return transactionTotalNoCommission + commissionAddon;
    }

    @Override
    public String getName() {
        return name;
    }

}
