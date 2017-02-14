package com.prototype.model.event;

import com.prototype.model.Address;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public abstract class ApartmentEvent extends AddressEvent {

    private String apartment;

    public ApartmentEvent(LocalDateTime dateEvent, Address address, String apartment) {
        super(dateEvent, address);
        this.apartment = apartment;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
