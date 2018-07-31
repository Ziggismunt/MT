package com.mt.oep.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class AccountLockerService{
    private ConcurrentMap<Long, ReentrantLock> map;

    public AccountLockerService() {
        this.map = new ConcurrentHashMap<>();
    }

    public ReentrantLock getLock(Account account){
        synchronized (account) {
            if (map.containsKey(account.getID())) {
                return map.get(account.getID());
            } else {
                ReentrantLock lock = new ReentrantLock();
                map.put(account.getID(), lock);
                return lock;
            }
        }
    }
}
