package com.investobank.services;

import com.investobank.exceptions.BrokerOrderAmountExceeds100Exception;
import com.investobank.exceptions.OrderNotMultipleOf10Exception;
import com.investobank.exceptions.ValidCommissionNotFoundException;
import com.investobank.model.Order;
import com.investobank.model.OrderOutcome;

import java.util.*;

public class OrderServiceImpl implements OrderService {

    public static final int ORDER_MULTIPLE_FACTOR = 10;

    private final List<BrokerService> brokers;

    private final AuditService auditService;

    public OrderServiceImpl(List<BrokerService> brokers, AuditService auditService) {
        this.brokers = Collections.unmodifiableList(brokers);
        this.auditService = auditService;
    }

    @Override
    public double executeOrder(Order order) throws OrderNotMultipleOf10Exception,
            BrokerOrderAmountExceeds100Exception, ValidCommissionNotFoundException {
        //validate whether order amount is multiple of 10
        if (order.getAmount() % ORDER_MULTIPLE_FACTOR != 0) {
            throw new OrderNotMultipleOf10Exception("Order amount not a multiple of 10 but " + order.getAmount());
        }

        //split orders into smaller ones if necessary (e.g. if order amount > 100)
        List<Order> splitOrders = order.split();
        double totalQuote = 0;
        for(Order splitOrder : splitOrders) {
            double minQuote = Double.MAX_VALUE;
            BrokerService selectedBroker = null;
            for (BrokerService brokerService : brokers) { //check which broker offers the best deal
                double currentQuote = brokerService.getQuote(splitOrder);
                if (currentQuote < minQuote) {
                    minQuote = currentQuote;
                    selectedBroker = brokerService;
                }
            }
            totalQuote+=minQuote;

            //audit the deal with the most affordable broker.
            auditService.auditBrokerOrder(new OrderOutcome(splitOrder, minQuote), selectedBroker);
        }

        auditService.auditClientOrder(new OrderOutcome(order, totalQuote));
        return totalQuote;
    }
}
