package com.investobank.services;

import com.investobank.model.OrderOutcome;

import java.util.Map;

public interface AuditService {
    Map<String, Double> getClientNetPositions();
    Map<String, Long> getDigicoinTransactionsByBroker();
    void auditOrder(OrderOutcome orderOutcome, BrokerService broker);
}
