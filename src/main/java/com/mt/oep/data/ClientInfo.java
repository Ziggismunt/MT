package com.mt.oep.data;

public class ClientInfo {
    private String name;
    private float amount;

    public ClientInfo(String name) {
        this.name = name;
        this.amount = 0;
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public int setAmount(float amount) {
        if (amount < 0) {
            System.out.println("Clients can't have negative amount of money!");
            return -1;
        }
        this.amount = amount;
        return 0;
    }
}
