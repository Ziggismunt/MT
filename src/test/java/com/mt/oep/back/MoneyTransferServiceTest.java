package com.mt.oep.back;

import com.mt.oep.data.Account;
import com.mt.oep.data.AccountRepository;
import com.mt.oep.back.PaymentStatus;
import com.mt.oep.data.AccountValidation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        a1 = new Account("A", 5000f);
        a2 = new Account("B", 300f);
        a3 = new Account("C");
        accountRepository.addNewAccount(a1);
        accountRepository.addNewAccount(a2);
        accountRepository.addNewAccount(a3);
    }

    @Test
    public void SendToHimself(){
        Assert.assertEquals(ErrorCause.TO_HIMSELF, moneyTransferService.sendMoney(a1, a1, 500f).getCauseOfError());
    }

    @Test
    public void SendZero(){
        Assert.assertEquals(ErrorCause.NEGATIVE, moneyTransferService.sendMoney(a1, a2, 0).getCauseOfError());
    }

    @Test
    public void SendNegative(){
        Assert.assertEquals(ErrorCause.NEGATIVE, moneyTransferService.sendMoney(a1, a2, -200f).getCauseOfError());
    }

    @Test
    public void TrySendNotEnoughMoney(){
        accountRepository.setClientsMoney(1L, 500f);
        Assert.assertEquals(ErrorCause.NOT_ENOUGH_MONEY, moneyTransferService.sendMoney(a1, a2, 1000f).getCauseOfError());
    }

    @Test
    public void OneNormalSend(){
        float firstAmount = 5000f;
        float secondAmount = 300f;
        float toSend = 1000f;
        accountRepository.setClientsMoney(1L, firstAmount);
        accountRepository.setClientsMoney(2L, secondAmount);
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend).getCauseOfError());
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
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend).getCauseOfError());
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a2, a1, toSend).getCauseOfError());
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
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a1, a2, toSend1).getCauseOfError());
        Assert.assertEquals(ErrorCause.OK, moneyTransferService.sendMoney(a2, a1, toSend2).getCauseOfError());
        Assert.assertEquals(firstAmount - toSend1 + toSend2, accountRepository.getClientsMoney(1L), 0.0001);
        Assert.assertEquals(secondAmount + toSend1 - toSend2, accountRepository.getClientsMoney(2L), 0.0001);
    }

    //TODO threads!

    private class TransferThread extends Thread{
        @Override
        public void run() {
            moneyTransferService.sendMoney(a1, a2, 100f);
            //moneyTransferService.sendMoney(2L, 3L, 50f);
            moneyTransferService.sendMoney(a2, a1, 50f);
        }
    }

    @Test
    public void ThirtyTransferThreads(){
        accountRepository.setClientsMoney(1L, 4000000f);
        accountRepository.setClientsMoney(2L, 1f);
        accountRepository.setClientsMoney(3L, 0f);
        for (int i = 0; i < 300; i++){
            TransferThread tt = new TransferThread();
            tt.run();
//            try {
//                //tt.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println(accountRepository.getClientsMoney(1L));
        System.out.println(accountRepository.getClientsMoney(2L));
        System.out.println(accountRepository.getClientsMoney(3L));



    }

}
