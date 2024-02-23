package bg.uni.sofia.fmi.mjt.splitwise.notification;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OweMoneyNotificationTest {
    @Test
    public void testEqualsAndHashCode() {
        OwedMoneyNotification notification1 = new FriendOwedMoney("lender1","lemons",10);
        OwedMoneyNotification notification2 = new FriendOwedMoney("lender1","lemons",10);
        OwedMoneyNotification notification3 = new FriendOwedMoney("lender2","oranges",20);

        assertEquals(notification1, notification2,"Notification1 and notification2 should be equal");
        assertNotEquals(notification1, notification3,"Notification1 and notification3 should not be equal");

        assertEquals(notification1.hashCode(), notification2.hashCode(),"Hashcodes should be equal");
    }
}
