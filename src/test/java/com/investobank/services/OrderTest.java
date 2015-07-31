package com.investobank.services;

import com.investobank.model.Order;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class OrderTest {

    @Test
    public void testOrderSplit_whenBuying(){
        Order order = new Order("Client A", 130);
        List<Order> splitOrders = order.split();
        assertEquals(2, splitOrders.size());
        for(Order splitOrder : splitOrders){
            assertTrue(splitOrder.getAmount() == 100 || splitOrder.getAmount() == 30);
        }
    }

    @Test
    public void testOrderSplit_whenSelling(){
        Order order = new Order("Client B", -211);
        List<Order> splitOrders = order.split();
        assertEquals(3, splitOrders.size());
        for(Order splitOrder : splitOrders){
            assertTrue(splitOrder.getAmount() == -100 || splitOrder.getAmount() == -11);
        }
    }
}
