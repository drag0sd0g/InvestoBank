package com.investobank.services;

import com.investobank.model.Order;
import com.investobank.model.OrderOutcome;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class AuditServiceTest {

    @Test
    public void testGettingDigicoinTransactionByBroker(){
        String clientName = "Fictive Client";
        BrokerService broker1 = new BrokerServiceImpl("broker 1", Collections.emptyList(), 100);
        BrokerService broker2 = new BrokerServiceImpl("broker 2", Collections.emptyList(), 200);
        AuditService auditService = new AuditServiceImpl();
        auditService.auditBrokerOrder(new OrderOutcome(new Order(clientName,50), 100), broker1);
        auditService.auditBrokerOrder(new OrderOutcome(new Order(clientName,150), 100), broker1);
        auditService.auditBrokerOrder(new OrderOutcome(new Order(clientName, 250), 200), broker2);
        Map<String, Long> digicoinTransactionsByBroker = auditService.getDigicoinTransactionsByBroker();
        assertEquals(200, digicoinTransactionsByBroker.get("broker 1").longValue());
        assertEquals(250, digicoinTransactionsByBroker.get("broker 2").longValue());
    }

    @Test
    public void testGettingClientNetPositions(){
        String clientName1 = "Fictive Client 1";
        String clientName2 = "Fictive Client 2";
        AuditService auditService = new AuditServiceImpl();
        auditService.auditClientOrder(new OrderOutcome(new Order(clientName1, 50), 123.2));
        auditService.auditClientOrder(new OrderOutcome(new Order(clientName1, 150), 160.26));
        auditService.auditClientOrder(new OrderOutcome(new Order(clientName1, 200), 194.43));
        auditService.auditClientOrder(new OrderOutcome(new Order(clientName2, 30), 47.79));
        Map<String, Double> clientNetPositions = auditService.getClientNetPositions();
        assertEquals(600.607, clientNetPositions.get(clientName1).doubleValue());
        assertEquals(47.79, clientNetPositions.get(clientName2).doubleValue());
    }

}
