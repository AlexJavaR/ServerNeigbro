package com.prototype.model.event.payment.split;

import com.prototype.model.User;

import java.time.LocalDateTime;

public class PayPalSplitPaymentEvent extends SplitPaymentEvent {
    public PayPalSplitPaymentEvent(LocalDateTime dateEvent, User author, Integer amount) {
        super(dateEvent, author, amount);
    }
}
