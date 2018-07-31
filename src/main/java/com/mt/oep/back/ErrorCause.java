package com.mt.oep.back;

public enum ErrorCause {
    NO_SENDER("Sender doesn't exist")
    , NO_RECEIVER("Receiver doesn't exist")
    , NEGATIVE("Tried to send zero or negativa")
    , NOT_ENOUGH_MONEY("Not enough money")
    , TO_HIMSELF("Tried to send to himself")
    , FAIL("Transaction failed")
    , OK("All is OK");

    private final String val;

    ErrorCause(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }

}
