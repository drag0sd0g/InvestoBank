package com.investobank.services;

import com.investobank.exceptions.BrokerOrderAmountExceeds100Exception;
import com.investobank.exceptions.OrderNotMultipleOf10Exception;
import com.investobank.exceptions.ValidCommissionNotFoundException;
import com.investobank.model.Order;

import java.util.*;

public class OrderServiceImpl implements OrderService {

    private final List<BrokerService> brokers;

    private final AuditService auditService;

    public OrderServiceImpl(List<BrokerService> brokers, AuditService auditService) {
        this.brokers = Collections.unmodifiableList(brokers);
        this.auditService = auditService;
    }

    @Override
    public double executeOrder(Order order) throws OrderNotMultipleOf10Exception,
            BrokerOrderAmountExceeds100Exception, ValidCommissionNotFoundException {
        if (order.getAmount() % 10 != 0) {
            throw new OrderNotMultipleOf10Exception("Order amount not a multiple of 10 but " + order.getAmount());
        }

        List<Order> splitOrders = order.split();
        double totalQuote = 0;
        for(Order splitOrder : splitOrders) {
            double minQuote = Double.MAX_VALUE;
            BrokerService selectedBroker = null;
            for (BrokerService brokerService : brokers) {
                double currentQuote = brokerService.getQuote(splitOrder);
                if (currentQuote < minQuote) {
                    minQuote = currentQuote;
                    selectedBroker = brokerService;
                }
            }
            totalQuote+=minQuote;
            auditService.auditOrder(order, selectedBroker);
        }

        return totalQuote;
    }
}
