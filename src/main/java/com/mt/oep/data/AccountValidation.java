package com.mt.oep.data;

import com.mt.oep.back.ErrorCause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class AccountValidation {
    private AccountRepository accountRepository;
    private Logger logger;

    public AccountValidation(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        logger = LoggerFactory.getLogger(AccountValidation.class);
    }

    public ErrorCause validate(Account account){
        if (!accountRepository.accountExists(account)){
            logger.error("Receiver doesn't exist");
            return ErrorCause.NO_RECEIVER;
        }
        return ErrorCause.OK;
    }

    public ErrorCause validate(Account account, BigDecimal sum){
        if (!accountRepository.accountExists(account)){
            logger.error("Sender doesn't exist");
            return ErrorCause.NO_SENDER;
        }
        if (sum.compareTo(new BigDecimal(0)) <= 0){
            logger.error("Money must be positive");
            return ErrorCause.NEGATIVE;
        }
        if (account.getAmount().compareTo(sum) < 0){
            logger.error("Not enough money");
            return ErrorCause.NOT_ENOUGH_MONEY;
        }
        return ErrorCause.OK;
    }


}
