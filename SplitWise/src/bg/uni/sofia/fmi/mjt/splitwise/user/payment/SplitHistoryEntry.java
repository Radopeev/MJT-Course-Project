package bg.uni.sofia.fmi.mjt.splitwise.user.payment;

import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledEditorKit;
import java.io.Serializable;

public record SplitHistoryEntry(String counterparty, String reason, double amount, PaymentType type) implements
    Serializable {
    @Override
    public String toString() {
        String paymentType = type.getValue();
        return String.format(
            "%s - %s: %s, Amount: %.2f",
            paymentType,
            counterparty,
            reason,
            amount
        );
    }
}
