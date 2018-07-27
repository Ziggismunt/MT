package com.mt.oep.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataModel {
    private ConcurrentMap<Long, ClientInfo> clients;
    private static Long maxID;

    public DataModel() {
        maxID = 0L;
        this.clients = new ConcurrentHashMap<>();
    }

    public void addNewClient(String name){
        maxID++;
        clients.putIfAbsent(maxID, new ClientInfo(name));
    }

    public int setClientsMoney(long id, float amount){
        if (! ifClientExist(id)){
            System.out.println("No such client!");
            return -1;
        }
        ClientInfo client = clients.get(id);
        return client.setAmount(amount);
    }

    public float getClientsMoney(long id){
        if (! ifClientExist(id)){
            System.out.println("No such client!");
            return -1f;
        }
        ClientInfo client = clients.get(id);
        return client.getAmount();
    }

    public boolean ifClientExist(long id){
        return clients.get(id) != null;

    }
}
