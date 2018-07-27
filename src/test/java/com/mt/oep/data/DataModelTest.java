package com.mt.oep.data;

import org.junit.Assert;
import org.junit.Test;

public class DataModelTest {

    @Test
    public void setNotExistingClient(){
        DataModel dm = new DataModel();
        dm.addNewClient("ABC");
        dm.addNewClient("DEF");

        Assert.assertEquals(0, dm.setClientsMoney(1L, 500f));
        Assert.assertEquals(-1, dm.setClientsMoney(3L, 500f));
    }

    @Test
    public void setNegativeMoney(){
        DataModel dm = new DataModel();
        dm.addNewClient("ABC");
        dm.addNewClient("DEF");

        Assert.assertEquals(0, dm.setClientsMoney(1L, 500f));
        Assert.assertEquals(-1, dm.setClientsMoney(1L, -500f));
    }
}
