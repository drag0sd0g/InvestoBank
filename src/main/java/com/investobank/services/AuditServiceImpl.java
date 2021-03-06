package com.investobank.services;

import com.investobank.model.OrderOutcome;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditServiceImpl implements AuditService {

    private final Map<String, List<OrderOutcome>> inMemoryClientOrderAudit;

    private final Map<String, List<OrderOutcome>> inMemoryBrokerOrderAudit;

    public AuditServiceImpl() {
        this.inMemoryClientOrderAudit = new HashMap<>();
        this.inMemoryBrokerOrderAudit = new HashMap<>();
    }

    @Override
    public Map<String, Double> getClientNetPositions() {
        Map<String, Double> rtn = new HashMap<>();
        for (Map.Entry<String, List<OrderOutcome>> clientOrderAuditEntry : inMemoryClientOrderAudit.entrySet()) {
            List<OrderOutcome> ordersForClient = clientOrderAuditEntry.getValue();
            Double sumOfTransactionsPerDigicoin = 0d;
            Double allAmounts = 0d;
            for (OrderOutcome orderOutcome : ordersForClient) {
                sumOfTransactionsPerDigicoin += orderOutcome.getPrice() / Math.abs(orderOutcome.getOrder().getAmount());
                allAmounts += orderOutcome.getOrder().getAmount();
            }
            Double average = new Double(sumOfTransactionsPerDigicoin / ordersForClient.size()) * allAmounts;
            rtn.put(clientOrderAuditEntry.getKey(), new BigDecimal(average).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        return rtn;
    }

    @Override
    public Map<String, Long> getDigicoinTransactionsByBroker() {
        Map<String, Long> rtn = new HashMap<>();
        for (Map.Entry<String, List<OrderOutcome>> brokerOrderAuditEntry : inMemoryBrokerOrderAudit.entrySet()) {
            List<OrderOutcome> ordersForBroker = brokerOrderAuditEntry.getValue();
            Long totalForBroker = 0l;
            for (OrderOutcome orderOutcome : ordersForBroker) {
                totalForBroker += Math.abs(orderOutcome.getOrder().getAmount());
            }
            rtn.put(brokerOrderAuditEntry.getKey(), totalForBroker);
        }
        return rtn;
    }

    @Override
    public void auditBrokerOrder(OrderOutcome orderOutcome, BrokerService broker) {
        doAuditOrder(inMemoryBrokerOrderAudit, broker.getName(), orderOutcome);
    }

    @Override
    public void auditClientOrder(OrderOutcome orderOutcome) {
        doAuditOrder(inMemoryClientOrderAudit, orderOutcome.getOrder().getClient(), orderOutcome);
    }

    private void doAuditOrder(Map<String, List<OrderOutcome>> auditMap, String name, OrderOutcome orderOutcome){
        List<OrderOutcome> orderOutcomes = auditMap.get(name);
        if(orderOutcomes == null){
            List<OrderOutcome> newOrderOutcomeList = new ArrayList<>();
            newOrderOutcomeList.add(orderOutcome);
            auditMap.put(name, newOrderOutcomeList);
        } else {
            orderOutcomes.add(orderOutcome);
        }
    }
}
