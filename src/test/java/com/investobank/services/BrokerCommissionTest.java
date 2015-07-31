package com.investobank.services;

import com.investobank.model.BrokerCommission;
import com.investobank.model.FixedBrokerComission;
import com.investobank.model.VariableBrokerComission;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BrokerCommissionTest {

    @Test
    public void testFixedBrokerCommissionCalculation(){
        BrokerCommission brokerCommission = new FixedBrokerComission(5.0);
        assertEquals(5.0, brokerCommission.decideCommission(Long.MAX_VALUE));
    }

    @Test
    public void testVariableBrokerCommissionCalculation(){
        BrokerCommission brokerCommission10to40 = new VariableBrokerComission(10, 40, 3.0);
        assertEquals(3.0, brokerCommission10to40.decideCommission(35));
        assertEquals(VariableBrokerComission.COMMISSION_NOT_APPLICABLE_FOR_THIS_RANGE,  brokerCommission10to40.decideCommission(41));
    }


}
