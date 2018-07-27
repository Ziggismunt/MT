package com.mt.oep.back;

import com.mt.oep.data.DataModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MoneyTransferTest {
    MoneyTransfer moneyTransfer;
    DataModel dataModel;

    @Before
    public void initialize(){
        dataModel = new DataModel();
        moneyTransfer = new MoneyTransfer(dataModel);
        dataModel.addNewClient("A");//has 5000
        dataModel.setClientsMoney(1L, 5000f);
        dataModel.addNewClient("B");//has 300
        dataModel.setClientsMoney(2L, 300f);
        dataModel.addNewClient("C");
        dataModel.addNewClient("D");
        dataModel.addNewClient("E");//has 0 money
    }

    @Test
    public void SendToHimself(){
        Assert.assertEquals(-1, moneyTransfer.sendMoney(1L, 1L, 500f));
    }

    @Test
    public void SendZero(){
        Assert.assertEquals(-1, moneyTransfer.sendMoney(1L, 2L, 0));
    }

    @Test
    public void SendNegative(){
        Assert.assertEquals(-1, moneyTransfer.sendMoney(1L, 2L, -200f));
    }

    @Test
    public void TrySendNotEnoughMoney(){
        dataModel.setClientsMoney(1L, 500f);
        Assert.assertEquals(-1, moneyTransfer.sendMoney(1L, 2L, 1000f));
    }

    @Test
    public void OneNormalSend(){
        float firstAmount = 5000f;
        float secondAmount = 300f;
        float toSend = 1000f;
        dataModel.setClientsMoney(1L, firstAmount);
        dataModel.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(0, moneyTransfer.sendMoney(1L, 2L, toSend));
        Assert.assertEquals(firstAmount - toSend, dataModel.getClientsMoney(1L), 0.0001);
        Assert.assertEquals(secondAmount + toSend, dataModel.getClientsMoney(2L), 0.0001);
    }

    @Test
    public void SendToEachOtherSameAmount(){
        float firstAmount = 5000f;
        float secondAmount = 300f;
        float toSend = 100f;
        dataModel.setClientsMoney(1L, firstAmount);
        dataModel.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(0, moneyTransfer.sendMoney(1L, 2L, toSend));
        Assert.assertEquals(0, moneyTransfer.sendMoney(2L, 1L, toSend));
        Assert.assertEquals(firstAmount, dataModel.getClientsMoney(1L), 0.0001);
        Assert.assertEquals(secondAmount, dataModel.getClientsMoney(2L), 0.0001);
    }

    @Test
    public void SendToEachOtherNotSameAmount(){
        float firstAmount = 5000f;
        float secondAmount = 300f;
        float toSend1 = 100f;
        float toSend2 = 50f;
        dataModel.setClientsMoney(1L, firstAmount);
        dataModel.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(0, moneyTransfer.sendMoney(1L, 2L, toSend1));
        Assert.assertEquals(0, moneyTransfer.sendMoney(2L, 1L, toSend2));
        Assert.assertEquals(firstAmount - toSend1 + toSend2, dataModel.getClientsMoney(1L), 0.0001);
        Assert.assertEquals(secondAmount + toSend1 - toSend2, dataModel.getClientsMoney(2L), 0.0001);
    }

    //TODO threads!

}
