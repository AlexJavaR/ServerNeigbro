package com.prototype.model.event.payment;

import com.prototype.model.Address;
import com.prototype.model.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public class HousematePayPalPaymentEvent extends HousematePaymentEvent {

    @DBRef
    private User author;

    public HousematePayPalPaymentEvent(LocalDateTime dateEvent, Address address, String apartment, Integer amount, User author) {
        super(dateEvent, address, apartment, amount);
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
