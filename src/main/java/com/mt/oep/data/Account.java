package com.mt.oep.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Account {
    private static AtomicLong maxID = new AtomicLong(0L);
    private String name;
    private Long ID;
    private BigDecimal amount;
    private Logger logger;

    public Account(String name) {
        this(name, new BigDecimal(0));
    }

    public Account(String name, BigDecimal amount){
        this.name = name;
        this.amount = amount;
        this.ID = maxID.incrementAndGet();
        logger = LoggerFactory.getLogger(Account.class);
    }

    public Long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal(0)) < 0) {
            //logger.error("Clients can't have negative amount of money!");
            throw new IllegalArgumentException("Clients can't have negative amount of money!");
        }
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name) &&
                Objects.equals(ID, account.ID) &&
               // Objects.equals(amount, account.amount) &&
                Objects.equals(logger, account.logger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ID, logger);
        //return Objects.hash(name, ID, amount, logger);
    }
}
