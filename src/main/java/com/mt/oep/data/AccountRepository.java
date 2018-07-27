package com.mt.oep.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class AccountRepository {
    private ConcurrentMap<Long, Account> clients;
    private static AtomicLong maxID;
    private Logger logger;

    public AccountRepository() {
        maxID = new AtomicLong(0L);
        this.clients = new ConcurrentHashMap<>();
        logger = LoggerFactory.getLogger(AccountRepository.class);
    }

    public void addNewClient(String name){
        clients.putIfAbsent(maxID.incrementAndGet(), new Account(name));
    }

    public int setClientsMoney(long id, float amount){
        if (! ifClientExist(id)){
            logger.error("No such client!");
            return -1;
        }
        Account client = clients.get(id);
        return client.setAmount(amount);
    }

    public float getClientsMoney(long id){
        if (! ifClientExist(id)){
            logger.error("No such client!");
            return -1f;
        }
        Account client = clients.get(id);
        return client.getAmount();
    }

    public boolean ifClientExist(long id){
        return clients.get(id) != null;

    }
}
