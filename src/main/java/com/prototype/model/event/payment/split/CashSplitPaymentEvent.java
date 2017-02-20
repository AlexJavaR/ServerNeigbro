package com.prototype.model.event.payment.split;

import com.prototype.model.User;

import java.time.LocalDateTime;

public class CashSplitPaymentEvent extends SplitPaymentEvent {
    public CashSplitPaymentEvent(LocalDateTime dateEvent, User author, Integer amount) {
        super(dateEvent, author, amount);
    }
}
