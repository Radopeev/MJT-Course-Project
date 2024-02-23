package bg.uni.sofia.fmi.mjt.splitwise.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ApprovedMoneyNotificationTest {
    @Test
    public void testHashCode() {
        ApprovedMoneyNotification notification1 = new FriendApprovedMoney("receiver1", "lemons",10);
        ApprovedMoneyNotification notification2 = new FriendApprovedMoney("receiver1", "lemons",10);

        assertEquals(notification1.hashCode(), notification2.hashCode(),"Hashcodes should be equal");
    }
}
