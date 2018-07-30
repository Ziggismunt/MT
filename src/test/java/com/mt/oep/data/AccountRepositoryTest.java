package com.mt.oep.data;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountRepositoryTest {

    @Test
    public void setNotExistingClient(){
        AccountRepository dm = new AccountRepository();
        dm.addNewClient("ABC");
        dm.addNewClient("DEF");

        Assert.assertEquals(0, dm.setClientsMoney(1L, new BigDecimal(500)));
        Assert.assertEquals(-1, dm.setClientsMoney(3L, new BigDecimal(500)));
    }

    @Test
    public void setNegativeMoney(){
        AccountRepository dm = new AccountRepository();
        dm.addNewClient("ABC");
        dm.addNewClient("DEF");

        Assert.assertEquals(0, dm.setClientsMoney(1L, new BigDecimal(500)));
        Assert.assertEquals(-1, dm.setClientsMoney(1L, new BigDecimal(-500)));
    }
}
