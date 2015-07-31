package com.investobank.services;

import com.investobank.exceptions.OrderNotMultipleOf10Exception;
import com.investobank.model.BrokerCommission;
import com.investobank.model.FixedBrokerComission;
import com.investobank.model.Order;
import com.investobank.model.VariableBrokerComission;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class OrderServiceTest {

    @Test(expected = OrderNotMultipleOf10Exception.class)
    public void testOrderAmountMultipleOf10_expectException() throws Exception{
        OrderService orderService = new OrderServiceImpl(Collections.emptyList(), null);
        orderService.executeOrder(new Order("client", 11));
    }

    @Test
    public void testSplittingLargeOrderBetweenBrokers() throws Exception{
        BrokerService broker1 = new BrokerServiceImpl("Broker 1", Arrays.asList(new FixedBrokerComission(5)), 1.49d);
        BrokerCommission variableBrokerComission10to40 = new VariableBrokerComission(10, 40, 3d);
        BrokerCommission variableBrokerComission50to80 = new VariableBrokerComission(50, 80, 2.5d);
        BrokerCommission variableBrokerComission90to100 = new VariableBrokerComission(90, 100, 2d);
        BrokerService broker2 = new BrokerServiceImpl("Broker 2", Arrays.asList(variableBrokerComission10to40,
                variableBrokerComission50to80,variableBrokerComission90to100), 1.52d);

        AuditService auditService = new AuditServiceImpl();
        OrderService orderService = new OrderServiceImpl(Arrays.asList(broker1, broker2), auditService);

        Order order = new Order("Client A", 130);
        assertEquals(201.975, orderService.executeOrder(order));

        Map<String, Long> digicoinTransactionByBroker = auditService.getDigicoinTransactionsByBroker();
        assertEquals(30, digicoinTransactionByBroker.get("Broker 1").longValue());
        assertEquals(100, digicoinTransactionByBroker.get("Broker 2").longValue());
    }

}
