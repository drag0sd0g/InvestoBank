package com.investobank.services;

import com.investobank.exceptions.BrokerOrderAmountExceeds100Exception;
import com.investobank.exceptions.OrderNotMultipleOf10Exception;
import com.investobank.exceptions.ValidCommissionNotFoundException;
import com.investobank.model.BrokerCommission;
import com.investobank.model.FixedBrokerComission;
import com.investobank.model.Order;
import com.investobank.model.VariableBrokerComission;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;


public class BrokerServiceTest {

    @Test(expected = OrderNotMultipleOf10Exception.class)
    public void testOrderAmountMultipleOf10_expectException() throws Exception{
        BrokerService brokerService = new BrokerServiceImpl("testBroker", Collections.emptyList(), 0);
        brokerService.getQuote(new Order("client", 99));
    }

    @Test(expected = BrokerOrderAmountExceeds100Exception.class)
    public void testOrderAmountLargerThan100_expectException() throws Exception{
        BrokerService brokerService = new BrokerServiceImpl("testBroker", Collections.emptyList(), 0);
        brokerService.getQuote(new Order("client", -200));
    }

    @Test
    public void testBrokerWithFixedCommission() throws Exception{
        BrokerService broker1 = new BrokerServiceImpl("Broker 1", Arrays.asList(new FixedBrokerComission(5)), 1.49d);
        Order order1 = new Order("Client A", 10);
        assertEquals(15.645, broker1.getQuote(order1));
        Order order2 = new Order("Client B", 40);
        assertEquals(62.58, broker1.getQuote(order2));
    }

    @Test (expected = ValidCommissionNotFoundException.class)
    public void testBrokerWithVariableCommissionAndOutOfRangeOrder() throws Exception{
        BrokerCommission variableBrokerComission10to40 = new VariableBrokerComission(10, 40, 3d);
        BrokerCommission variableBrokerComission60to80 = new VariableBrokerComission(60, 80, 2.5d);
        BrokerService brokerService = new BrokerServiceImpl("Broker", Arrays.asList(variableBrokerComission10to40,
                variableBrokerComission60to80), 1.52d);
        brokerService.getQuote(new Order("Client X", 50));
    }


}
