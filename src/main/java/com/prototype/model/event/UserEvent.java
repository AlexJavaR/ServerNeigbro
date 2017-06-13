package com.prototype.model.event;

import com.prototype.model.Address;
import com.prototype.model.User;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Event")
public abstract class UserEvent extends AddressEvent {

    @DBRef(lazy = true)
    private User author;
    private String titleEvent;
    private String description;

    public UserEvent(String titleEvent, LocalDateTime dateEvent, Address address, User author, String description) {
        super(dateEvent, address);
        this.author = author;
        this.titleEvent = titleEvent;
        this.description = description;
    }

    //@JsonIgnore
    public User getAuthor() {
        return author;
    }

    public String getTitleEvent() {
        return titleEvent;
    }

    public void setTitleEvent(String titleEvent) {
        this.titleEvent = titleEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
