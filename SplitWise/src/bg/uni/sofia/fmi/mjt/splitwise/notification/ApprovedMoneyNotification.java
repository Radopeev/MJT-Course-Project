package bg.uni.sofia.fmi.mjt.splitwise.notification;

import bg.uni.sofia.fmi.mjt.splitwise.dueamount.DueAmount;

import java.io.Serializable;

public class ApprovedMoneyNotification implements Notification, Serializable {
    private String friend;
    private DueAmount dueAmount;
    public ApprovedMoneyNotification(String friend, DueAmount dueAmount){
        this.friend = friend;
        this.dueAmount = dueAmount;
    }
    @Override
    public String inform() {
        return String.format("%s approved your payment %s LV [%s]",friend,dueAmount.getAmount(),dueAmount.getReason());
    }
}
