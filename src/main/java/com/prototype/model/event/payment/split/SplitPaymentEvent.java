package com.prototype.model.event.payment.split;

import com.prototype.model.User;
import com.prototype.model.event.GlobalEvent;

import java.time.LocalDateTime;

public abstract class SplitPaymentEvent extends GlobalEvent {
    private Integer amount;

    public SplitPaymentEvent(LocalDateTime dateEvent, User author, Integer amount) {
        super(dateEvent, author);
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
