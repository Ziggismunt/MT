package com.mt.oep.back;

import java.util.Date;

public class PaymentStatus {
    private Status state;
    private Date transferEnd;
    private ErrorCause causeOfError;


    public PaymentStatus(ErrorCause cause) {
        if (cause.equals(ErrorCause.OK)) {
            state = Status.SUCCESS;
        }
        else {
            state = Status.ERROR;
        }
        this.causeOfError = cause;
        transferEnd = new Date();
    }

    public Status getState() {
        return state;
    }

    public Date getTransferEnd() {
        return transferEnd;
    }

    public ErrorCause getCauseOfError() {
        return causeOfError;
    }

    enum Status {SUCCESS, ERROR}

}
