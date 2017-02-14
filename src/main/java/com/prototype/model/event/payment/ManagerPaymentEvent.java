package com.prototype.model.event.payment;

import com.prototype.model.Address;
import com.prototype.model.User;
import com.prototype.model.event.ManagerEvent;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public class ManagerPaymentEvent extends ManagerEvent {

    private Integer totalAmount;
    private boolean fromFundAddress;

    public ManagerPaymentEvent(String titleEvent, LocalDateTime dateEvent, Address address, User author, String description, Integer totalAmount, boolean fromFundAddress) {
        super(titleEvent, dateEvent, address, author, description);
        this.totalAmount = totalAmount;
        this.fromFundAddress = fromFundAddress;
        setPersonal(false);
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isFromFundAddress() {
        return fromFundAddress;
    }

    public void setFromFundAddress(boolean fromFundAddress) {
        this.fromFundAddress = fromFundAddress;
    }
}
