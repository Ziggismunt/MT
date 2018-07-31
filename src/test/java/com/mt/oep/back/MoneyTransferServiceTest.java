package com.mt.oep.back;

import com.mt.oep.data.Account;
import com.mt.oep.data.AccountLockerService;
import com.mt.oep.data.AccountRepository;
import com.mt.oep.back.PaymentStatus;
import com.mt.oep.data.AccountValidation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MoneyTransferServiceTest {
    MoneyTransferService moneyTransferService;
    AccountValidation accountValidation;
    AccountLockerService accountLockerService;
    AccountRepository accountRepository;
    Account a1;
    Account a2;
    Account a3;

    @Before
    public void initialize() {
        accountRepository = new AccountRepository();
        accountValidation = new AccountValidation(accountRepository);
        accountLockerService = new AccountLockerService();
        moneyTransferService = new MoneyTransferService(accountValidation, accountLockerService);
        a1 = new Account("A", new BigDecimal(5000));
        a2 = new Account("B", new BigDecimal(300));
        a3 = new Account("C");
        accountRepository.addNewAccount(a1);
        accountRepository.addNewAccount(a2);
        accountRepository.addNewAccount(a3);
    }

    @Test
    public void SendToHimself(){
        Assert.assertEquals(ErrorCause.TO_HIMSELF, moneyTransferService.sendMoney(a1, a1, new BigDecimal(500)).getCauseOfError());
    }

    @Test
    public void SendZero(){
        Assert.assertEquals(ErrorCause.NEGATIVE, moneyTransferService.sendMoney(a1, a2, new BigDecimal(0)).getCauseOfError());
    }

    @Test
    public void SendNegative(){
        Assert.assertEquals(ErrorCause.NEGATIVE, moneyTransferService.sendMoney(a1, a2, new BigDecimal(-200)).getCauseOfError());
    }

    @Test
    public void TrySendNotEnoughMoney(){
        accountRepository.setClientsMoney(1L, new BigDecimal(500));
        Assert.assertEquals(ErrorCause.NOT_ENOUGH_MONEY, moneyTransferService.sendMoney(a1, a2, new BigDecimal(1000)).getCauseOfError());
    }

    @Test
    public void OneNormalSend(){
        BigDecimal firstAmount = new BigDecimal(5000);
        BigDecimal secondAmount = new BigDecimal(300);
        BigDecimal toSend = new BigDecimal(1000);
        a1.setAmount(firstAmount);
        a2.setAmount(secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend).getCauseOfError());
        Assert.assertEquals(firstAmount.subtract(toSend), accountRepository.getAccount(a1.getID()).getAmount());
        Assert.assertEquals(secondAmount.add(toSend), accountRepository.getAccount(a2.getID()).getAmount());
    }

    @Test
    public void SendToEachOtherSameAmount(){
        BigDecimal firstAmount = new BigDecimal(5000);
        BigDecimal secondAmount = new BigDecimal(300);
        BigDecimal toSend = new BigDecimal(100);
        a1.setAmount(firstAmount);
        a2.setAmount(secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend).getCauseOfError());
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a2, a1, toSend).getCauseOfError());
        Assert.assertEquals(firstAmount, accountRepository.getAccount(a1.getID()).getAmount());
        Assert.assertEquals(secondAmount, accountRepository.getAccount(a2.getID()).getAmount());
    }

    @Test
    public void SendToEachOtherNotSameAmount(){
        BigDecimal firstAmount = new BigDecimal(5000);
        BigDecimal secondAmount = new BigDecimal(300);
        BigDecimal toSend1 = new BigDecimal(100);
        BigDecimal toSend2 = new BigDecimal(50);
        a1.setAmount(firstAmount);
        a2.setAmount(secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend1).getCauseOfError());
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a2, a1, toSend2).getCauseOfError());
        Assert.assertEquals(firstAmount.subtract(toSend1).add(toSend2), accountRepository.getAccount(a1.getID()).getAmount());
        Assert.assertEquals(secondAmount.add(toSend1).subtract(toSend2), accountRepository.getAccount(a2.getID()).getAmount());
    }

    //TODO threads!

    private class myRunnable implements Runnable{
        @Override
        public void run() {
            moneyTransferService.sendMoney(a1, a2, new BigDecimal(100));
            moneyTransferService.sendMoney(a2, a3, new BigDecimal(50));
            moneyTransferService.sendMoney(a2, a1, new BigDecimal(50));
        }
    }

    @Test
    public void ThirtyTransferThreads(){
        BigDecimal firstAmount = new BigDecimal(4000000);
        BigDecimal secondAmount = new BigDecimal(1);
        BigDecimal thirdAmount = new BigDecimal(0);
        accountRepository.setClientsMoney(1L, firstAmount);
        accountRepository.setClientsMoney(2L, secondAmount);
        accountRepository.setClientsMoney(3L, thirdAmount);
        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 300; i++){
            Runnable runnable = new myRunnable();
            executor.execute(runnable);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println(accountRepository.getAccount(a1.getID()).getAmount());
        System.out.println(accountRepository.getAccount(a2.getID()).getAmount());
        System.out.println(accountRepository.getAccount(a3.getID()).getAmount());
        Assert.assertEquals(firstAmount.add(secondAmount).add(thirdAmount), a1.getAmount().add(a2.getAmount()).add(a3.getAmount()));



    }

}
