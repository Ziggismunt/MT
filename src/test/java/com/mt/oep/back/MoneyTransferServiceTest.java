package com.mt.oep.back;

import com.mt.oep.data.AccountRepository;
import com.mt.oep.back.PaymentStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MoneyTransferServiceTest {
    MoneyTransferService moneyTransferService;
    AccountRepository accountRepository;

    @Before
    public void initialize(){
        accountRepository = new AccountRepository();
        moneyTransferService = new MoneyTransferService(accountRepository);
        accountRepository.addNewClient("A");//has 5000
        accountRepository.setClientsMoney(1L, 5000f);
        accountRepository.addNewClient("B");//has 300
        accountRepository.setClientsMoney(2L, 300f);
        accountRepository.addNewClient("C");
        accountRepository.addNewClient("D");
        accountRepository.addNewClient("E");//has 0 money
    }

    @Test
    public void SendToHimself(){
        Assert.assertEquals(ErrorCause.TO_HIMSELF, moneyTransferService.sendMoney(1L, 1L, 500f).getCauseOfError());
    }

    @Test
    public void SendZero(){
        Assert.assertEquals(ErrorCause.NEGATIVE, moneyTransferService.sendMoney(1L, 2L, 0).getCauseOfError());
    }

    @Test
    public void SendNegative(){
        Assert.assertEquals(ErrorCause.NEGATIVE, moneyTransferService.sendMoney(1L, 2L, -200f).getCauseOfError());
    }

    @Test
    public void TrySendNotEnoughMoney(){
        accountRepository.setClientsMoney(1L, 500f);
        Assert.assertEquals(ErrorCause.NOT_ENOUGH_MONEY, moneyTransferService.sendMoney(1L, 2L, 1000f).getCauseOfError());
    }

    @Test
    public void OneNormalSend(){
        float firstAmount = 5000f;
        float secondAmount = 300f;
        float toSend = 1000f;
        accountRepository.setClientsMoney(1L, firstAmount);
        accountRepository.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(1L, 2L, toSend).getCauseOfError());
        Assert.assertEquals(firstAmount - toSend, accountRepository.getClientsMoney(1L), 0.0001);
        Assert.assertEquals(secondAmount + toSend, accountRepository.getClientsMoney(2L), 0.0001);
    }

    @Test
    public void SendToEachOtherSameAmount(){
        float firstAmount = 5000f;
        float secondAmount = 300f;
        float toSend = 100f;
        accountRepository.setClientsMoney(1L, firstAmount);
        accountRepository.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(1L, 2L, toSend).getCauseOfError());
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(2L, 1L, toSend).getCauseOfError());
        Assert.assertEquals(firstAmount, accountRepository.getClientsMoney(1L), 0.0001);
        Assert.assertEquals(secondAmount, accountRepository.getClientsMoney(2L), 0.0001);
    }

    @Test
    public void SendToEachOtherNotSameAmount(){
        float firstAmount = 5000f;
        float secondAmount = 300f;
        float toSend1 = 100f;
        float toSend2 = 50f;
        accountRepository.setClientsMoney(1L, firstAmount);
        accountRepository.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(1L, 2L, toSend1).getCauseOfError());
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(2L, 1L, toSend2).getCauseOfError());
        Assert.assertEquals(firstAmount - toSend1 + toSend2, accountRepository.getClientsMoney(1L), 0.0001);
        Assert.assertEquals(secondAmount + toSend1 - toSend2, accountRepository.getClientsMoney(2L), 0.0001);
    }

    //TODO threads!

}
