package com.investobank.services;

import com.investobank.model.Order;

import java.util.Map;

public interface AuditService {
    Map<String, Double> getClientNetPositions();
    Map<String, Long> getDigicoinTransactionsByBroker();
    void auditOrder(Order order, BrokerService broker);
}
