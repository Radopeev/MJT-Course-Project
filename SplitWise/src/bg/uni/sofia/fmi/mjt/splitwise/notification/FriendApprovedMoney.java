package bg.uni.sofia.fmi.mjt.splitwise.notification;

public class FriendApprovedMoney extends ApprovedMoneyNotification {
    public FriendApprovedMoney(String receiver, String reason, double amount) {
        super(receiver, reason, amount);
    }

    @Override
    public String inform() {
        return String.format("%s approved your payment %s LV [%s]", receiver, amount, reason);
    }
}
