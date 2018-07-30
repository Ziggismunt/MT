package com.mt.oep.back;

import com.mt.oep.data.Account;
import com.mt.oep.data.AccountValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mt.oep.data.AccountRepository;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.math.BigDecimal;


public class MoneyTransferService {
    private AccountValidation accountValidation;
    private Logger logger;

    public MoneyTransferService(AccountValidation accountValidation) {
        this.accountValidation = accountValidation;
        logger = LoggerFactory.getLogger(MoneyTransferService.class);
    }

    public PaymentStatus sendMoney(Account clientFrom, Account clientTo, BigDecimal money){
        // primitive checks
        if (clientFrom.equals(clientTo)){
            logger.error("You can't send money to yourself");
            return new PaymentStatus(ErrorCause.TO_HIMSELF);
        }
        ErrorCause errorCause = accountValidation.validate(clientFrom, money);
        if (errorCause != ErrorCause.OK){
            return new PaymentStatus(errorCause);
        }
        errorCause = accountValidation.validate(clientTo);
        if (errorCause != ErrorCause.OK){
            return new PaymentStatus(errorCause);
        }
        // end of primitive checks


        synchronized (this){
            BigDecimal newAmountSender = clientFrom.getAmount().subtract(money);
            BigDecimal newAmountReceiver = clientTo.getAmount().add(money);
            clientFrom.setAmount(newAmountSender);
            clientTo.setAmount(newAmountReceiver);
        }
        logger.info("Perfect transaction");

        return new PaymentStatus(ErrorCause.OK);
    }


}
