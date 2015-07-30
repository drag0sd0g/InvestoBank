package com.investobank.services;

import org.junit.Test;

import java.util.Collections;

public class OrderServiceTest {

    @Test(expected = OrderNotMultipleOf10Exception.class)
    public void testOrderAmountMultipleOf10_expectException() throws Exception{
        OrderService orderService = new OrderServiceImpl(Collections.emptyList());
        orderService.executeOrder(new Order("client", 11, OrderType.SELL));
    }

}
