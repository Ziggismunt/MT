package com.mt.oep.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Account {
    private String name;
    private float amount;
    private Logger logger;

    public Account(String name) {
        this.name = name;
        this.amount = 0;
        logger = LoggerFactory.getLogger(Account.class);
    }

    public Account(String name, float amount){
        this.name = name;
        this.amount = amount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Float.compare(account.amount, amount) == 0 &&
                Objects.equals(name, account.name) &&
                Objects.equals(logger, account.logger);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, amount, logger);
    }
}
