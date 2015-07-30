package com.investobank.services;

import java.util.Collections;
import java.util.List;

public class BrokerServiceImpl {

    private final List<BrokerCommission> brokerRules;

    private final double digicoinQuote;

    public BrokerServiceImpl(List<BrokerCommission> brokerRules, double digicoinQuote) {
        this.brokerRules = Collections.unmodifiableList(brokerRules);
        this.digicoinQuote = digicoinQuote;
    }
}
