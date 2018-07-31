package com.mt.oep.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccountRepository {
    private ConcurrentMap<Long, Account> clients;
    private Logger logger;

    public AccountRepository() {
        this.clients = new ConcurrentHashMap<>();
        logger = LoggerFactory.getLogger(AccountRepository.class);
    }

    public void addNewAccount(Account account){
        clients.putIfAbsent(account.getID(), account);
    }

    public void setClientsMoney(long id, BigDecimal amount){
        if (! ifClientExist(id)){
            throw new IllegalArgumentException("No such client!");
        }
        Account client = clients.get(id);
        client.setAmount(amount);
    }

    public Account getAccount(Long id){
        return clients.get(id);
    }

    public boolean ifClientExist(long id){
        return clients.get(id) != null;
    }

    public boolean accountExists(Account account){
        return clients.containsValue(account);
    }
}
