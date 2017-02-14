package com.prototype.model.event;

import com.prototype.model.Address;
import com.prototype.model.User;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public abstract class ManagerEvent extends UserEvent
{
    public ManagerEvent(String titleEvent, LocalDateTime dateEvent, Address address, User author, String description) {
        super(titleEvent, dateEvent, address, author, description);
    }
}
