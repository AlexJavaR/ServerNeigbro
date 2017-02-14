package com.prototype.model.event.announcement;

import com.prototype.model.Address;
import com.prototype.model.User;
import com.prototype.model.event.UserEvent;

import java.time.LocalDateTime;

public abstract class AnnouncementEvent extends UserEvent {

    public AnnouncementEvent(String titleEvent, LocalDateTime dateEvent, Address address, User author, String description) {
        super(titleEvent, dateEvent, address, author, description);
        setPersonal(false);
    }
}
