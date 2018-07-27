package com.mt.oep.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Account {
    private String name;
    private float amount;
    private Logger logger;

    public Account(String name) {
        this.name = name;
        this.amount = 0;
        logger = LoggerFactory.getLogger(Account.class);
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public int setAmount(float amount) {
        if (amount < 0) {
            logger.error("Clients can't have negative amount of money!");
            return -1;
        }
        this.amount = amount;
        return 0;
    }
}
