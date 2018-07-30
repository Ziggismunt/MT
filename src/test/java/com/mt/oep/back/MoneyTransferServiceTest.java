package com.mt.oep.back;

import com.mt.oep.data.Account;
import com.mt.oep.data.AccountRepository;
import com.mt.oep.back.PaymentStatus;
import com.mt.oep.data.AccountValidation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class MoneyTransferServiceTest {
    MoneyTransferService moneyTransferService;
    AccountValidation accountValidation;
    AccountRepository accountRepository;
    Account a1;
    Account a2;
    Account a3;

    @Before
    public void initialize() {
        accountRepository = new AccountRepository();
        accountValidation = new AccountValidation(accountRepository);
        moneyTransferService = new MoneyTransferService(accountValidation);
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
        accountRepository.setClientsMoney(1L, firstAmount);
        accountRepository.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend).getCauseOfError());
        Assert.assertEquals(firstAmount.subtract(toSend), accountRepository.getAccount(1L).getAmount());
        Assert.assertEquals(secondAmount.add(toSend), accountRepository.getAccount(2L).getAmount());
    }

    @Test
    public void SendToEachOtherSameAmount(){
        BigDecimal firstAmount = new BigDecimal(5000);
        BigDecimal secondAmount = new BigDecimal(300);
        BigDecimal toSend = new BigDecimal(100);
        accountRepository.setClientsMoney(1L, firstAmount);
        accountRepository.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend).getCauseOfError());
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a2, a1, toSend).getCauseOfError());
        Assert.assertEquals(firstAmount, accountRepository.getAccount(1L).getAmount());
        Assert.assertEquals(secondAmount, accountRepository.getAccount(2L).getAmount());
    }

    @Test
    public void SendToEachOtherNotSameAmount(){
        BigDecimal firstAmount = new BigDecimal(5000);
        BigDecimal secondAmount = new BigDecimal(300);
        BigDecimal toSend1 = new BigDecimal(100);
        BigDecimal toSend2 = new BigDecimal(50);
        accountRepository.setClientsMoney(1L, firstAmount);
        accountRepository.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend1).getCauseOfError());
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a2, a1, toSend2).getCauseOfError());
        Assert.assertEquals(firstAmount.subtract(toSend1).add(toSend2), accountRepository.getAccount(1L).getAmount());
        Assert.assertEquals(secondAmount.add(toSend1).subtract(toSend2), accountRepository.getAccount(2L).getAmount());
    }

    //TODO threads!

    private class TransferThread extends Thread{
        @Override
        public void run() {
            moneyTransferService.sendMoney(a1, a2, new BigDecimal(100));
            //moneyTransferService.sendMoney(2L, 3L, 50f);
            moneyTransferService.sendMoney(a2, a1, new BigDecimal(50));
        }
    }

    @Test
    public void ThirtyTransferThreads(){
        accountRepository.setClientsMoney(1L, new BigDecimal(4000000));
        accountRepository.setClientsMoney(2L, new BigDecimal(1));
        accountRepository.setClientsMoney(3L, new BigDecimal(0));
        for (int i = 0; i < 300; i++){
            TransferThread tt = new TransferThread();
            tt.run();
//            try {
//                //tt.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println(accountRepository.getAccount(1L).getAmount());
        System.out.println(accountRepository.getAccount(2L).getAmount());
        System.out.println(accountRepository.getAccount(3L).getAmount());



    }

}
