package bg.uni.sofia.fmi.mjt.splitwise.notification;

import bg.uni.sofia.fmi.mjt.splitwise.dueamount.DueAmount;

import java.io.Serializable;

public class OwedMoneyNotification implements Notification, Serializable {

    private String friend;
    private DueAmount dueAmount;
    public OwedMoneyNotification(String friend, DueAmount dueAmount){
        this.friend = friend;
        this.dueAmount = dueAmount;
    }
    @Override
    public String inform() {
        return String.format("%s owes you %s LV [%s]",friend,dueAmount.getAmount(),dueAmount.getReason());
    }
}
