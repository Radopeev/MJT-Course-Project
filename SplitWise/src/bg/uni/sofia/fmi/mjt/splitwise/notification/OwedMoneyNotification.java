package bg.uni.sofia.fmi.mjt.splitwise.notification;

import java.io.Serializable;

public abstract class OwedMoneyNotification implements Notification, Serializable {

    protected String reason;
    protected double amount;
    public OwedMoneyNotification(String reason, double amount) {
        this.reason = reason;
        this.amount = amount;
    }
}
