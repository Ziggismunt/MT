package com.mt.oep.back;

import com.mt.oep.data.DataModel;

public class MoneyTransfer {
    private DataModel dataModel;

    public MoneyTransfer(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public int sendMoney(long clientFrom, long clientTo, float money){
        // primitive checks
        if (!dataModel.ifClientExist(clientFrom)){
            System.out.println("ClientFrom doesn't exist");
            return -1;
        }
        if (!dataModel.ifClientExist(clientTo)){
            System.out.println("ClientTo doesn't exist");
            return -1;
        }
        if (money <= 0){
            System.out.println("Money must be positive");
            return -1;
        }
        if (clientFrom == clientTo){
            System.out.println("You can't send money to yourself");
            return -1;
        }
        if (dataModel.getClientsMoney(clientFrom) < money){
            System.out.println("Not enough money");
            return -1;
        }
        // end of primitive checks

        synchronized (this){
            float newAmount1 = dataModel.getClientsMoney(clientFrom) - money;
            float newAmount2 = dataModel.getClientsMoney(clientTo) + money;
            dataModel.setClientsMoney(clientFrom, newAmount1);
            dataModel.setClientsMoney(clientTo, newAmount2);
        }

        return 0;
    }


}
