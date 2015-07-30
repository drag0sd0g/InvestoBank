package com.investobank.services;

import com.investobank.exceptions.BrokerOrderAmountExceeds100Exception;
import com.investobank.exceptions.OrderNotMultipleOf10Exception;
import com.investobank.exceptions.ValidCommissionNotFoundException;
import com.investobank.model.Order;

public interface OrderService {
    double executeOrder(Order order) throws OrderNotMultipleOf10Exception, BrokerOrderAmountExceeds100Exception, ValidCommissionNotFoundException;
}
