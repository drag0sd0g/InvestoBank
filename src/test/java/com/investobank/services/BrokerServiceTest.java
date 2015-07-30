package com.investobank.services;

import com.investobank.exceptions.BrokerOrderAmountExceeds100Exception;
import com.investobank.exceptions.OrderNotMultipleOf10Exception;
import com.investobank.model.Order;
import org.junit.Test;

import java.util.Collections;


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

}
