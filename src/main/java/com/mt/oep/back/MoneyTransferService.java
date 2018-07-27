package com.mt.oep.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mt.oep.data.AccountRepository;

public class MoneyTransferService {
    private AccountRepository accountRepository;
    private Logger logger;

    public MoneyTransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        logger = LoggerFactory.getLogger(MoneyTransferService.class);
    }

    public PaymentStatus sendMoney(long clientFrom, long clientTo, float money){
        // primitive checks
        if (!accountRepository.ifClientExist(clientFrom)){
            logger.error("Sender doesn't exist");
            return new PaymentStatus(ErrorCause.NO_SENDER);
        }
        if (!accountRepository.ifClientExist(clientTo)){
            logger.error("Receiver doesn't exist");
            return new PaymentStatus(ErrorCause.NO_RECEIVER);
        }
        if (money <= 0){
            logger.error("Money must be positive");
            return new PaymentStatus(ErrorCause.NEGATIVE);
        }
        if (clientFrom == clientTo){
            logger.error("You can't send money to yourself");
            return new PaymentStatus(ErrorCause.TO_HIMSELF);
        }
        if (accountRepository.getClientsMoney(clientFrom) < money){
            logger.error("Not enough money");
            return new PaymentStatus(ErrorCause.NOT_ENOUGH_MONEY);
        }
        // end of primitive checks

        synchronized (this){
            float newAmount1 = accountRepository.getClientsMoney(clientFrom) - money;
            float newAmount2 = accountRepository.getClientsMoney(clientTo) + money;
            accountRepository.setClientsMoney(clientFrom, newAmount1);
            accountRepository.setClientsMoney(clientTo, newAmount2);
        }
        logger.info("Perfect transaction");

        return new PaymentStatus(ErrorCause.OK);
    }


}
