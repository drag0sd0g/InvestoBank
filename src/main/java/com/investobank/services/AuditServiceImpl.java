package com.investobank.services;

import com.investobank.model.Order;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditServiceImpl implements AuditService {

    private final Map<String, List<Order>> inMemoryClientOrderAudit;

    private final Map<String, List<Order>> inMemoryBrokerOrderAudit;

    public AuditServiceImpl() {
        this.inMemoryClientOrderAudit = new HashMap<>();
        this.inMemoryBrokerOrderAudit = new HashMap<>();
    }

    @Override
    public Map<String, Double> getClientNetPositions() {
        Map<String, Double> rtn = new HashMap<>();
        for (Map.Entry<String, List<Order>> clientOrderAuditEntry : inMemoryClientOrderAudit.entrySet()) {
            List<Order> ordersForClient = clientOrderAuditEntry.getValue();
            Long sumOfTransactions = 0l;
            for (Order order : ordersForClient) {
                sumOfTransactions += order.getAmount();
            }
            rtn.put(clientOrderAuditEntry.getKey(), (double) (sumOfTransactions * ordersForClient.size()));
        }
        return rtn;
    }

    @Override
    public Map<String, Long> getDigicoinTransactionsByBroker() {
        Map<String, Long> rtn = new HashMap<>();
        for (Map.Entry<String, List<Order>> brokerOrderAuditEntry : inMemoryBrokerOrderAudit.entrySet()) {
            List<Order> ordersForBroker = brokerOrderAuditEntry.getValue();
            Long totalForBroker = 0l;
            for (Order order : ordersForBroker) {
                totalForBroker += Math.abs(order.getAmount());
            }
            rtn.put(brokerOrderAuditEntry.getKey(), totalForBroker);
        }
        return rtn;
    }

    @Override
    public void auditOrder(Order order, BrokerService broker) {
        doAuditOrder(inMemoryClientOrderAudit, order.getClient(), order);
        doAuditOrder(inMemoryBrokerOrderAudit, broker.getName(), order);
    }

    private void doAuditOrder(Map<String, List<Order>> auditMap, String name, Order order){
        List<Order> orders = auditMap.get(name);
        if(orders == null){
            auditMap.put(name, Arrays.asList(order));
        } else {
            orders.add(order);
        }
    }
}
