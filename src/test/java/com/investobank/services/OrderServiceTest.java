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

    @Test
    public void testSelectingBestBrokerDeal() throws Exception{
        BrokerService broker1 = new BrokerServiceImpl("Broker 1", Arrays.asList(new FixedBrokerComission(5)), 1.49d);
        BrokerCommission variableBrokerComission10to40 = new VariableBrokerComission(10, 40, 3d);
        BrokerCommission variableBrokerComission50to80 = new VariableBrokerComission(50, 80, 2.5d);
        BrokerCommission variableBrokerComission90to100 = new VariableBrokerComission(90, 100, 2d);
        BrokerService broker2 = new BrokerServiceImpl("Broker 2", Arrays.asList(variableBrokerComission10to40,
                variableBrokerComission50to80, variableBrokerComission90to100), 1.52d);
        AuditService auditService = new AuditServiceImpl();
        OrderService orderService = new OrderServiceImpl(Arrays.asList(broker1, broker2), auditService);
        Order order1 = new Order("Client B", 40);
        assertEquals(62.58, orderService.executeOrder(order1));
        Order order2 = new Order("Client A", 50);
        assertEquals(77.9, orderService.executeOrder(order2));

        Map<String, Long> digicoinTransactionByBroker = auditService.getDigicoinTransactionsByBroker();
        assertEquals(40, digicoinTransactionByBroker.get("Broker 1").longValue());
        assertEquals(50, digicoinTransactionByBroker.get("Broker 2").longValue());

    }

    @Test
    public void acceptanceTestSuite() throws Exception{

        //build broker1
        BrokerService broker1 = new BrokerServiceImpl("Broker 1", Arrays.asList(new FixedBrokerComission(5)), 1.49d);

        //build broker2
        BrokerCommission variableBrokerComission10to40 = new VariableBrokerComission(10, 40, 3d);
        BrokerCommission variableBrokerComission50to80 = new VariableBrokerComission(50, 80, 2.5d);
        BrokerCommission variableBrokerComission90to100 = new VariableBrokerComission(90, 100, 2d);
        BrokerService broker2 = new BrokerServiceImpl("Broker 2", Arrays.asList(variableBrokerComission10to40,
                variableBrokerComission50to80,variableBrokerComission90to100), 1.52d);

        AuditService auditService = new AuditServiceImpl();
        OrderService orderService = new OrderServiceImpl(Arrays.asList(broker1, broker2), auditService);

        //1) Client A buys 10 at 15.645
        Order order1 = new Order("Client A", 10);
        assertEquals(15.645, orderService.executeOrder(order1));

        //2) Client B buys 40 at 62.58
        Order order2 = new Order("Client B", 40);
        assertEquals(62.58, orderService.executeOrder(order2));

        //3) Client A buys 50 at 77.9
        Order order3 = new Order("Client A", 50);
        assertEquals(77.9, orderService.executeOrder(order3));

        //4) Client B buys 100 at 155.04
        Order order4 = new Order("Client B", 100);
        assertEquals(155.04, orderService.executeOrder(order4));

        //5) Client B sells 80 at 124.64
        Order order5 = new Order("Client B", -80);
        assertEquals(124.64, orderService.executeOrder(order5));

        //6) Client C sells 70 at 109.06
        Order order6 = new Order("Client C", -70);
        assertEquals(109.06, orderService.executeOrder(order6));

        //7) Client A buys 130 at 201.975
        Order order7 = new Order("Client A", 130);
        assertEquals(201.975, orderService.executeOrder(order7));

        //8) Client B sells 60 at 93.48
        Order order8 = new Order("Client B", -60);
        assertEquals(93.48, orderService.executeOrder(order8));

        //9) Report client net positions: Client A 296.156 Client B 0 Client C -109.06
        Map<String, Double> netPositions = auditService.getClientNetPositions();
        assertEquals(296.156, netPositions.get("Client A"));
        assertEquals(0.0, netPositions.get("Client B"));
        assertEquals(-109.06, netPositions.get("Client C"));

        //10) Report number of Digicoins transacted by Broker: Broker 1 80 Broker 2 460
        Map<String, Long> digicoinTransactionsByBroker = auditService.getDigicoinTransactionsByBroker();
        assertEquals(80, digicoinTransactionsByBroker.get("Broker 1").longValue());
        assertEquals(460, digicoinTransactionsByBroker.get("Broker 2").longValue());
    }

}
