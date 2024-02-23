package bg.uni.sofia.fmi.mjt.splitwise.notification;

import java.util.Objects;

public class FriendOwedMoney extends OwedMoneyNotification {
    private String lender;
    public FriendOwedMoney(String lender, String reason, double amount) {
        super(reason, amount);
        this.lender = lender;
    }

    @Override
    public String inform() {
        return String.format("You owe %s %s LV [%s]", lender, amount, reason);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendOwedMoney that = (FriendOwedMoney) o;
        return Objects.equals(lender, that.lender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lender);
    }
}
