package com.mt.oep.back;

import com.mt.oep.data.Account;
import com.mt.oep.data.AccountLockerService;
import com.mt.oep.data.AccountValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mt.oep.data.AccountRepository;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class MoneyTransferService {
    private AccountValidation accountValidation;
    private Logger logger;
    private AccountLockerService lockerService;

    public MoneyTransferService(AccountValidation accountValidation, AccountLockerService accountLockerService) {
        this.accountValidation = accountValidation;
        this.lockerService = accountLockerService;
        logger = LoggerFactory.getLogger(MoneyTransferService.class);
    }

    public PaymentStatus sendMoney(Account clientFrom, Account clientTo, BigDecimal money){
        // primitive checks
        if (clientFrom.equals(clientTo)){
            logger.error("You can't send money to yourself");
            return new PaymentStatus(ErrorCause.TO_HIMSELF);
        }
        ErrorCause errorCause = accountValidation.validate(clientTo);
        if (errorCause != ErrorCause.OK){
            return new PaymentStatus(errorCause);
        }
        // end of primitive checks

        ReentrantLock lockFrom = lockerService.getLock(clientFrom);
        ReentrantLock lockTo = lockerService.getLock(clientTo);
        for (int i = 0; i < 5; i++) {
            try {
                if (lockFrom.tryLock()) {
                    try {
                        if (lockTo.tryLock()) {
                            try{
                                errorCause = accountValidation.validate(clientFrom, money);
                                if (errorCause != ErrorCause.OK){
                                    return new PaymentStatus(errorCause);
                                }
                                BigDecimal newAmountSender = clientFrom.getAmount().subtract(money);
                                BigDecimal newAmountReceiver = clientTo.getAmount().add(money);
                                clientFrom.setAmount(newAmountSender);
                                clientTo.setAmount(newAmountReceiver);
                                return new PaymentStatus(ErrorCause.OK);
                                //break;
                            }
                            finally {
                                lockTo.unlock();
                            }
                        }
                    }
                    finally {
                        lockFrom.unlock();
                    }
                }
                Thread.sleep(100);
            } catch (IllegalArgumentException e){
                logger.error(e.getMessage());
            }
            catch (InterruptedException | RuntimeException e) {
                e.printStackTrace();
            }
        }

        logger.info("Transaction failed");
        return new PaymentStatus(ErrorCause.FAIL);

/*         //old
        synchronized (this){
            BigDecimal newAmountSender = clientFrom.getAmount().subtract(money);
            BigDecimal newAmountReceiver = clientTo.getAmount().add(money);
            clientFrom.setAmount(newAmountSender);
            clientTo.setAmount(newAmountReceiver);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
    }


}
