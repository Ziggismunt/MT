package com.mt.oep.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private String name;
    private BigDecimal amount;
    private Logger logger;

    public Account(String name) {
        this.name = name;
        this.amount = new BigDecimal(0);
        logger = LoggerFactory.getLogger(Account.class);
    }

    public Account(String name, BigDecimal amount){
        this.name = name;
        this.amount = amount;
        logger = LoggerFactory.getLogger(Account.class);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int setAmount(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal(0)) < 0) {
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
        return account.amount.compareTo(amount) == 0 &&
                Objects.equals(name, account.name) &&
                Objects.equals(logger, account.logger);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, amount, logger);
    }
}
