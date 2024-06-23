package bg.uni.sofia.fmi.mjt.splitwise.notification;

import java.io.Serializable;
import java.util.Objects;

public abstract class ApprovedMoneyNotification implements Notification, Serializable {
    protected String receiver;
    protected String reason;
    protected double amount;
    public ApprovedMoneyNotification(String receiver, String reason, double amount) {
        this.receiver = receiver;
        this.reason = reason;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApprovedMoneyNotification that = (ApprovedMoneyNotification) o;
        return Objects.equals(receiver, that.receiver)
            && Objects.equals(reason, that.reason)
            && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiver, reason, amount);
    }
}
