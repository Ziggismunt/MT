package com.mt.oep.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountRepositoryTest {
    private AccountRepository accountRepository;
    private Account a1;
    private Account a2;


    @Before
    public void initialize(){
        accountRepository = new AccountRepository();
        a1 = new Account("ABC");
        a2 = new Account("DEF");
        accountRepository.addNewAccount(a1);
        accountRepository.addNewAccount(a2);
    }



    @Test
    public void setNotExistingClient(){
        System.out.println(a1.getID());
        Assert.assertEquals(0, accountRepository.setClientsMoney(1L, new BigDecimal(500)));
        Assert.assertEquals(-1, accountRepository.setClientsMoney(3L, new BigDecimal(500)));
    }

    @Test
    public void setNegativeMoney(){
        Assert.assertEquals(0, accountRepository.setClientsMoney(1L, new BigDecimal(500)));
        Assert.assertEquals(-1, accountRepository.setClientsMoney(1L, new BigDecimal(-500)));
    }


}
