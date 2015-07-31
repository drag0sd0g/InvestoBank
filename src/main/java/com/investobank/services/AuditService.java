package com.investobank.services;

import com.investobank.model.OrderOutcome;

import java.util.Map;

public interface AuditService {
    Map<String, Double> getClientNetPositions();
    Map<String, Long> getDigicoinTransactionsByBroker();
    void auditBrokerOrder(OrderOutcome orderOutcome, BrokerService broker);
    void auditClientOrder(OrderOutcome orderOutcome);
}
